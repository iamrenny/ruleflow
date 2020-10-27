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
        logger.info("locking workflow for edition $countryCode-$workflowName")

        return isWorkflowBeingEdited(countryCode, workflowName)
            .flatMap { exists ->
                if (exists)
                    getUserEditing(countryCode, workflowName).flatMap { userEditing ->
                        if (user == userEditing)
                            lockWorkflowToEdit(countryCode, workflowName, user)
                        else
                            Single.just(
                                WorkflowEditionStatus(
                                    status = "NOT OK",
                                    message = "workflow is being edited by $userEditing"
                                )
                            )
                    }
                else
                    lockWorkflowToEdit(
                        countryCode,
                        workflowName,
                        user
                    )
            }
    }

    private fun lockWorkflowToEdit(countryCode: String, workflowName: String, user: String): Single<WorkflowEditionStatus> {
        return redisClient.rxSetex(buildKey(countryCode, workflowName), ttl, user)
            .map { result ->
                if (result == "OK")
                    WorkflowEditionStatus(
                        "OK",
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

    fun unlockWorkflowEdition(countryCode: String, workflowName: String): Single<WorkflowEditionStatus> {
        logger.info("unlocking workflow editing $countryCode-$workflowName")

        return redisClient.rxDel(buildKey(countryCode, workflowName))
            .map {
                WorkflowEditionStatus(
                    "OK",
                    "workflow edition unlocked"
                )
            }
    }

    private fun buildKey(countryCode: String, workflowName: String)
        = "$prefix$countryCode$workflowName"

    data class WorkflowEditionStatus(
        val status: String? = "NOT OK",
        val message: String? = "can not edit workflow"
    )
}