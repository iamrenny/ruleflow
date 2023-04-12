package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Single
import io.vertx.reactivex.redis.RedisClient
import javax.inject.Inject

class WorkflowEditionService @Inject constructor(private val redisClient: RedisClient) {
    private val logger by LoggerDelegate()

    private val ttl = 300L
    private val prefix = "rules_engine_workflow_being_edited_"

    fun lockWorkflowEdition(countryCode: String, workflowName: String, user: String): Single<WorkflowEditionStatus> {
        logger.info("locking workflow for edition $countryCode-$workflowName-$user")

        return isWorkflowBeingEdited(countryCode, workflowName)
            .flatMap { exists ->
                if (exists)
                    getUserEditing(countryCode, workflowName).flatMap { userEditing ->
                        if (user == userEditing)
                            lockWorkflowEditing(countryCode, workflowName, user)
                        else
                            Single.just(
                                WorkflowEditionStatus(
                                    status = MSG_NOT_OK,
                                    message = "workflow is being edited by $userEditing"
                                )
                            )
                    }
                else
                    lockWorkflowEditing(
                        countryCode,
                        workflowName,
                        user
                    )
            }
    }

    fun cancelWorkflowEdition(countryCode: String, workflowName: String, user: String): Single<WorkflowEditionStatus> {
        logger.info("cancelling workflow edition $countryCode-$workflowName-$user")

        return isWorkflowBeingEdited(countryCode, workflowName)
            .flatMap { exists ->
                if (exists)
                    getUserEditing(countryCode, workflowName).flatMap { userEditing ->
                        if (user == userEditing)
                            cancelWorkflowEditing(countryCode, workflowName)
                        else
                            Single.just(
                                WorkflowEditionStatus(
                                    status = MSG_NOT_OK,
                                    message = "workflow is being edited by $userEditing"
                                )
                            )
                    }
                else
                    cancelWorkflowEditing(
                        countryCode,
                        workflowName
                    )
            }
    }

    private fun lockWorkflowEditing(countryCode: String, workflowName: String, user: String): Single<WorkflowEditionStatus> {
        return redisClient.rxSetex(buildKey(countryCode, workflowName), ttl, user)
            .map { result ->
                if (result == MSG_OK)
                    WorkflowEditionStatus(
                        MSG_OK,
                        "workflow is being edited by $user"
                    )
                else
                    WorkflowEditionStatus(
                        message = result
                    )
            }
    }

    fun getUserEditing(countryCode: String, workflowName: String): Single<String> {
        return redisClient.rxGet(buildKey(countryCode, workflowName)).toSingle("NOT FOUND")
    }

    private fun isWorkflowBeingEdited(countryCode: String, workflowName: String): Single<Boolean> {
        return redisClient.rxExists(buildKey(countryCode, workflowName))
            .map { it > 0 }
    }

    fun cancelWorkflowEditing(countryCode: String, workflowName: String): Single<WorkflowEditionStatus> {
        return redisClient.rxDel(buildKey(countryCode, workflowName))
            .map {
                WorkflowEditionStatus(
                    MSG_OK,
                    "workflow edition canceled"
                )
            }
    }

    private fun buildKey(countryCode: String, workflowName: String) =
        "$prefix$countryCode$workflowName"

    data class WorkflowEditionStatus(
        val status: String? = MSG_NOT_OK,
        val message: String? = "can not edit workflow"
    )

    companion object {
        const val MSG_OK = "OK"
        const val MSG_NOT_OK = "NOT OK"
    }
}
