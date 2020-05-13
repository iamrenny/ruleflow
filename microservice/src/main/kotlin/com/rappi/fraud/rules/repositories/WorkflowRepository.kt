package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.GetListOfAllWorkflowsRequest
import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.entities.WorkflowInfo
import com.rappi.fraud.rules.entities.WorkflowKey
import io.reactivex.Observable
import io.reactivex.Single

class WorkflowRepository @Inject constructor(private val database: Database) {

    companion object {
        private const val INSERT = """
            INSERT INTO workflows (name, version, workflow, country_code, user_id) 
                 VALUES ($1, (SELECT COALESCE(MAX(version), 0) + 1 FROM workflows w WHERE w.country_code = $2 AND w.name = $3), $4, $5, $6) 
              RETURNING id, name, version, workflow, country_code, user_id, created_at
            """

        private const val GET_BY_KEY = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2 
               AND version = $3
            """

        private const val GET_ALL = """
            SELECT *
              FROM workflows
             WHERE country_code = $1
               AND name = $2
             ORDER BY version DESC
             LIMIT 10
             """

        private const val GET_WORKFLOW_LIST = """
            SELECT name, MAX(VERSION) as version
              FROM workflows
             WHERE country_code = $1
              GROUP BY name
              LIMIT 30
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
        return database.executeWithParams(INSERT, params).map { Workflow(it) }
    }

    fun get(workflowKey: WorkflowKey): Single<Workflow> {
        val params = listOf(
                workflowKey.countryCode,
                workflowKey.name,
                workflowKey.version!!
        )
        return database.get(GET_BY_KEY, params)
                .map { Workflow(it) }
                .firstOrError()
    }

    fun getAll(workflowRequest: GetAllWorkflowRequest): Observable<Workflow> {
        val params = listOf(
                workflowRequest.countryCode,
                workflowRequest.name
        )
        return database.get(GET_ALL, params).map {
            Workflow(it)
        }
    }

    fun getListOfAllWorkflows(workflowRequest: GetListOfAllWorkflowsRequest): Observable<WorkflowInfo> {
        val params = listOf(
            workflowRequest.countryCode
        )
        return database.get(GET_WORKFLOW_LIST, params).map {
            WorkflowInfo(it)
        }
    }
}
