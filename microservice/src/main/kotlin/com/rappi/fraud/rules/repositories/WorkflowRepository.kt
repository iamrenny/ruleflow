package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import java.util.concurrent.TimeUnit

class WorkflowRepository @Inject constructor(private val database: Database) {

    fun save(workflow: Workflow): Single<Workflow> {
        // TODO: Change versioning for is extremely inefficient
        val insertWorkflow = """
            INSERT INTO workflows (name, version, workflow, country_code, user_id) 
                 VALUES ($1::VARCHAR, (SELECT COALESCE(MAX(version), 0) + 1 FROM workflows w WHERE w.country_code = $3 AND w.name = $1::VARCHAR), $2, $3, $4) 
              RETURNING id, name, version, workflow, country_code, user_id, created_at
            """

        val params = listOf(
                workflow.name,
                workflow.workflowAsString!!,
                workflow.countryCode!!,
                workflow.userId!!
        )

        return database.executeWithParams(insertWorkflow, params)
            .map { Workflow(it) }
    }

    fun getWorkflow(countryCode: String, name: String, version: Long): Single<Workflow> {

        val getWorkflow = """
            SELECT w.*,
                   CASE WHEN aw.workflow_id isnull THEN false ELSE true END AS is_active
            FROM workflows w
            left JOIN active_workflows aw ON w.id = aw.workflow_id
            WHERE w.country_code = $1
              AND w.name = $2
              AND w.version = $3
            """
        val startTimeInMillis = System.currentTimeMillis()
        val params = listOf(
                countryCode,
                name,
                version
        )
        return database.get(getWorkflow, params)
                .map { Workflow(it) }
                .firstOrError()
                .doAfterTerminate {
                    BackendRegistries.getDefaultNow().timer("fraud.rules.engine.workflowRepository.get")
                        .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }

    fun exists(countryCode: String, name: String): Single<Boolean> {

        val getWorkflow = """
            SELECT w.*,
                   CASE WHEN aw.workflow_id isnull THEN false ELSE true END AS is_active
            FROM workflows w
            left JOIN active_workflows aw ON w.id = aw.workflow_id
            WHERE w.country_code = $1
              AND w.name = $2
            """
        val params = listOf(
            countryCode,
            name
        )
        return database.get(getWorkflow, params).isEmpty.map { !it }
    }

    fun getWorkflowsByCountryAndName(workflowRequest: GetAllWorkflowRequest): Observable<Workflow> {
        val getWorkflowsByCountryAndName = """
            SELECT w.*,
                   CASE WHEN aw.workflow_id isnull THEN false ELSE true END AS is_active
            FROM workflows w
            left JOIN active_workflows aw ON w.id = aw.workflow_id
            WHERE w.country_code = $1
              AND w.name = $2
              ORDER BY version DESC
             LIMIT 15
             """

        val params = listOf(
                workflowRequest.countryCode,
                workflowRequest.name
        )
        return database.get(getWorkflowsByCountryAndName, params).map {
            Workflow(it)
        }
    }

    fun getActiveWorkflowsByCountry(countryCode: String): Observable<Workflow> {

        val getActiveWorkflows = """
            SELECT w.name as name, true AS is_active
            FROM workflows w
            INNER JOIN active_workflows aw ON w.id = aw.workflow_id
            WHERE w.country_code = $1
             """
        val params = listOf(countryCode)

        return database.get(getActiveWorkflows, params).map {
            Workflow(it)
        }
    }

    fun getAllWorkflowsByCountry(countryCode: String): Observable<Workflow> {

        val getActiveAndInactiveWorkflows = """
                SELECT distinct(wo.name) AS name,
                       COALESCE(
                                  (SELECT TRUE
                                   FROM workflows w
                                   INNER JOIN active_workflows aw ON w.id = aw.workflow_id
                                   WHERE w.country_code = wo.country_code
                                     AND w.name = wo.name),FALSE) AS is_active
                FROM workflows wo
                WHERE wo.country_code = $1
             """
        val params = listOf(countryCode)

        return database.get(getActiveAndInactiveWorkflows, params).map {
            Workflow(it)
        }
    }
}
