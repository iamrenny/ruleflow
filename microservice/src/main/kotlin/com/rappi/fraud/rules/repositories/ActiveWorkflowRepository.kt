package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.ActiveKey
import com.rappi.fraud.rules.entities.ActiveWorkflow
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveWorkflowRepository @Inject constructor(private val database: Database) {

    companion object {
        private const val INSERT = """
            INSERT INTO active_workflows (country_code, name, workflow_id) 
                 VALUES ($1, $2, $3)
            ON CONFLICT (country_code, name)
              DO UPDATE SET workflow_id = EXCLUDED.workflow_id
              RETURNING country_code, name, workflow_id
        """

        private const val GET_BY_KEY = """
            SELECT aw.country_code, aw.name, aw.workflow_id, w.workflow 
              FROM active_workflows aw, workflows w
             WHERE aw.workflow_id = w.id
               AND aw.country_code = $1
               AND aw.name = $2
        """
    }

    fun save(workflow: ActiveWorkflow): Single<ActiveWorkflow> {
        val params = listOf(
            workflow.countryCode,
            workflow.name,
            workflow.workflowId
        )
        return database.executeWithParams(INSERT, params).map { ActiveWorkflow(it) }
    }

    fun get(key: ActiveKey): Single<ActiveWorkflow> {
        val startTimeInMillis = System.currentTimeMillis()
        val params = listOf(
            key.countryCode,
            key.name
        )
        return database.get(GET_BY_KEY, params)
            .map { ActiveWorkflow(it) }
            .firstOrError().doAfterTerminate {
                BackendRegistries.getDefaultNow().timer("fraud.rules.engine.activeWorkflowRepository.get")
                    .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }
}
