package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Single
import io.vertx.reactivex.redis.RedisClient
import javax.inject.Inject

class WorkflowEditingService @Inject constructor(private val redisClient: RedisClient) {
    private val logger by LoggerDelegate()

    private val ttl = 300L
    private val prefix = "rules_engine_workflow_being_edited_"

    fun lockWorkflowEditing(lockWorkflowEditionRequest: LockWorkflowEditingRequest): Single<LockWorkflowEditingResponse> {
        logger.info("locking workflow editing-${lockWorkflowEditionRequest}")

        val countryCode = lockWorkflowEditionRequest.countryCode
        val workflowName = lockWorkflowEditionRequest.workflowName
        val user = lockWorkflowEditionRequest.user

        return isWorkflowBeingEdited(countryCode, workflowName)
            .flatMap { exists ->
                if (exists)
                    getUserEditing(countryCode, workflowName).flatMap { userEditing ->
                        if (user == userEditing)
                            lockWorkflowBeingEdited(countryCode, workflowName, user)
                        else
                            Single.just(
                                LockWorkflowEditingResponse(
                                    status = "NOT OK",
                                    message = "workflow editing locked by $userEditing"
                                )
                            )
                    }
                else
                    lockWorkflowBeingEdited(
                        lockWorkflowEditionRequest.countryCode,
                        lockWorkflowEditionRequest.workflowName,
                        lockWorkflowEditionRequest.user
                    )
            }
    }

    fun unlockWorkflowEditing(unlockWorkflowEditingRequest: UnlockWorkflowEditingRequest): Single<LockWorkflowEditingResponse> {
        logger.info("unlocking workflow editing-${unlockWorkflowEditingRequest}")

        return redisClient.rxDel(buildKey(unlockWorkflowEditingRequest.countryCode, unlockWorkflowEditingRequest.workflowName))
            .map {
                LockWorkflowEditingResponse(
                    "OK",
                    "workflow editing unlocked"
                )
            }
    }

    private fun lockWorkflowBeingEdited(countryCode: String, workflowName: String, user: String): Single<LockWorkflowEditingResponse> {
        return redisClient.rxSetex(buildKey(countryCode, workflowName), ttl, user)
            .map { result ->
                if (result == "OK")
                    LockWorkflowEditingResponse(
                        "OK",
                        "workflow editing locked by $user"
                    )
                else
                    LockWorkflowEditingResponse(
                        message = result
                    )
            }
    }

    private fun getUserEditing(countryCode: String, workflowName: String): Single<String> {
        return redisClient.rxGet(buildKey(countryCode, workflowName)).toSingle("not found")
    }

    private fun isWorkflowBeingEdited(countryCode: String, workflowName: String): Single<Boolean> {
        return redisClient.rxExists(buildKey(countryCode, workflowName))
            .map { it > 0 }
    }

    private fun buildKey(countryCode: String, workflowName: String)
        = "$prefix$countryCode$workflowName"

    data class LockWorkflowEditingRequest(
        val countryCode: String,
        val workflowName: String,
        val user: String
    )

    data class UnlockWorkflowEditingRequest(
        val countryCode: String,
        val workflowName: String
    )

    data class LockWorkflowEditingResponse(
        val status: String? = "NOT OK",
        val message: String? = "can not lock workflow editing"
    )
}