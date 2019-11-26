package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowKey
import io.reactivex.Observable
import io.reactivex.Single

class WorkflowRepository @Inject constructor(private val database: Database) {

    companion object {
        private val INSERT_WORKFLOW = """
            INSERT INTO workflows (name, version, workflow, country_code, user_id) 
                 VALUES ($1, (SELECT COALESCE(MAX(version), 0) + 1 FROM workflows w WHERE w.country_code = $2 AND w.name = $3), $4, $5, $6) 
              RETURNING id, name, version, workflow, country_code, user_id
            """

        private val GET_WORKFLOW = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2 
               AND version = $3
            """

        private val GET_ALL_WORKFLOWS = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2
             ORDER BY version DESC
             LIMIT 10
             """
    }

    fun save(workflow: Workflow): Single<Workflow> {
        val params = listOf(
            workflow.name,
            workflow.countryCode,
            workflow.name,
            workflow.workflow,
            workflow.countryCode,
            workflow.userId
        )
        return database.save(INSERT_WORKFLOW, params).map { Workflow(it) }
    }

    fun get(workflowKey: WorkflowKey): Single<Workflow> {
        val params = listOf(
            workflowKey.countryCode,
            workflowKey.name,
            workflowKey.version
        )
        return database.get(GET_WORKFLOW, params)
            .map { Workflow(it) }
            .firstOrError()
    }

    fun getAll(workflowRequest: GetAllWorkflowRequest): Observable<Workflow> {
        val params = listOf(
            workflowRequest.countryCode,
            workflowRequest.name
        )
        return database.get(GET_ALL_WORKFLOWS, params).map {
            Workflow(
                it
            )
        }
    }
}