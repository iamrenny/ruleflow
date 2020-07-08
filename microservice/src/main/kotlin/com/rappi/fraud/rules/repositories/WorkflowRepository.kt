package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowInfo
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import java.util.concurrent.TimeUnit

class WorkflowRepository @Inject constructor(private val database: Database) {

    fun save(workflow: Workflow): Single<Workflow> {
        // TODO: Change versioning for is extremely inefficient
        val INSERT = """
            INSERT INTO workflows (name, version, workflow, country_code, user_id) 
                 VALUES ($1::VARCHAR, (SELECT COALESCE(MAX(version), 0) + 1 FROM workflows w WHERE w.country_code = $3 AND w.name = $1::VARCHAR), $2, $3, $4) 
              RETURNING id, name, version, workflow, country_code, user_id, created_at
            """

        val params = listOf(
                workflow.name,
                workflow.workflowAsString,
                workflow.countryCode,
                workflow.userId
        )

        return database.executeWithParams(INSERT, params)
            .map { Workflow(it) }
    }

    fun get(countryCode: String, name: String, version: Long): Single<Workflow> {

        val GET_BY_KEY = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2 
               AND version = $3
            """
        val startTimeInMillis = System.currentTimeMillis()
        val params = listOf(
                countryCode,
                name,
                version
        )
        return database.get(GET_BY_KEY, params)
                .map { Workflow(it) }
                .firstOrError()
                .doAfterTerminate {
                    BackendRegistries.getDefaultNow().timer("fraud.rules.engine.workflowRepository.get")
                        .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }

    fun getAll(workflowRequest: GetAllWorkflowRequest): Observable<Workflow> {
        val GET_ALL = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2
             ORDER BY version DESC
             LIMIT 10
             """

        val params = listOf(
                workflowRequest.countryCode,
                workflowRequest.name
        )
        return database.get(GET_ALL, params).map {
            Workflow(it)
        }
    }

    fun getListOfAllWorkflows(workflowRequest: GetListOfAllWorkflowsRequest): Observable<WorkflowInfo> {

        val GET_ACTIVE_WORKFLOW_LIST = """
            SELECT w.name as name, w.version as version
            FROM workflows w
            INNER JOIN active_workflows aw ON w.id = aw.workflow_id
            WHERE w.country_code = $1
            ORDER BY name ASC
            LIMIT 30
             """
        val params = listOf(
            workflowRequest.countryCode
        )
        return database.get(GET_ACTIVE_WORKFLOW_LIST, params).map {
            WorkflowInfo(it)
        }
    }
}
