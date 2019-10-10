package com.rappi.fraud.rules.rappi

import com.google.inject.Inject
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.rappi.entities.*
import com.rappi.fraud.rules.rappi.repositories.WorkflowRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject

class WorkflowsService @Inject constructor(
    val workflowRepository: WorkflowRepository

) {

    fun save(workflow: CreateWorkflowRequest): Single<WorkflowResponse> {
        val workflowBuilder = StringBuilder()
        workflowBuilder.append("workflow '")
        workflowBuilder.append(workflow.workflow)
        workflowBuilder.appendln("'")
        workflowBuilder.append("ruleset '")
        workflowBuilder.append(workflow.ruleset)
        workflowBuilder.appendln("'")
        workflow.rules.forEach {
            workflowBuilder.append(it.name)
            workflowBuilder.append(" ")
            workflowBuilder.append(it.condition)
            workflowBuilder.appendln()
        }
        workflowBuilder.append("end")
        return workflowRepository.save(workflow.workflow, workflowBuilder.toString()).map {
            WorkflowResponse(
                name = it.name,
                version = it.version,
                id = it.id,
                workflow = parseWorkflow(it.workflow)
            )
        }
    }

    fun get(workflow: GetWorkflowRequest): Single<WorkflowResponse> {
        return workflowRepository.get(workflow).map {
            WorkflowResponse(
                name = it.name,
                version = it.version,
                id = it.id,
                workflow = parseWorkflow(it.workflow)
            )
        }
    }

    fun getAll(workflow: GetAllWorkflowRequest): Observable<Workflow> {
        return workflowRepository.getAll(workflow)
    }

    fun evaluate(a: JsonObject, workflow: EvaluateWorkflowRequest): Single<String> {
        return workflowRepository.get(GetWorkflowRequest(name = workflow.name, version = workflow.version))
            .map {
                RuleEngine(it.workflow).evaluate(a.map)
            }
    }

    fun parseWorkflow(ws: String): CreateWorkflowRequest {
        val parts = ws.split("\n")
        val wflow = parts[0].split(" ")[1].replace("'", "")
        val rset = parts[1].split(" ")[1].replace("'", "")
        val rules = IntRange(2, parts.size - 2).map {
            val ruleParts = parts[it].split(" ")
            CreateWorkflowRuleRequest(
                name = ruleParts[0],
                condition = ruleParts.subList(1, ruleParts.size).joinToString(" ")
            )
        }
        return CreateWorkflowRequest(
            workflow = wflow,
            ruleset = rset,
            rules = rules
        )
    }
}
