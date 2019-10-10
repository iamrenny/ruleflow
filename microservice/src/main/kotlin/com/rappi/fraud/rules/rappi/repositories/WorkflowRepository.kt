package com.rappi.fraud.rules.rappi.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.bd.Database
import com.rappi.fraud.rules.rappi.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.rappi.entities.GetWorkflowRequest
import com.rappi.fraud.rules.rappi.entities.Workflow
import io.reactivex.Observable
import io.reactivex.Single

class WorkflowRepository @Inject constructor(
    val database: Database
) {

    private val INSERT_WORKFLOW = """INSERT INTO 
        |workflows (name, version, workflow) 
        |VALUES ($1,(SELECT COALESCE(MAX(version), 0) + 1 from workflows w where w.name = $2),$3) 
        |RETURNING id, name, version, workflow""".trimMargin()

    private val GET_WORKFLOW = """
        SELECT id, name,version, workflow
        FROM workflows
        WHERE name=$1 and version = $2
    """.trimIndent()

    private val GET_ALL_WORKFLOWS = """
        SELECT id, name, version, workflow
        FROM workflows
        WHERE name = $1
        ORDER BY version DESC
        LIMIT 10
    """.trimIndent()

    fun save(name: String, workflow: String): Single<Workflow> {
        val params = listOf(
            name,
            name,
            workflow
        )
        return database.save(INSERT_WORKFLOW, params).map { Workflow(it) }
    }

    fun get(workflow: GetWorkflowRequest): Single<Workflow> {
        val params = listOf(
            workflow.name,
            workflow.version
        )
        return database.get(GET_WORKFLOW, params).map { Workflow(it) }.firstOrError()
    }

    fun getAll(workflowRequest: GetAllWorkflowRequest): Observable<Workflow> {
        val params = listOf(
            workflowRequest.name
        )
        return database.get(GET_ALL_WORKFLOWS, params).map { Workflow(it) }
    }
}
