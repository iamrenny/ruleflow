package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.Workflow
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveWorkflowRepository @Inject constructor(private val database: Database) {
    fun save(workflow: Workflow): Single<Workflow> {

        val insert = """
            INSERT INTO active_workflows 
                 VALUES ($1, $2, $3)
            ON CONFLICT (country_code, name)
              DO UPDATE SET workflow_id = EXCLUDED.workflow_id
              RETURNING country_code, name, workflow_id
        """

        val params = listOf(
            workflow.countryCode!!,
            workflow.name,
            workflow.id!!
        )
        return database.executeWithParams(insert, params)
            .map { workflow.activate() }
    }

    fun get(countryCode: String, name: String): Single<Workflow> {
        val GET_BY_KEY = """
            SELECT w.id, w.country_code, w.name, w.version, w.workflow, w.user_id, w.created_at, true  
              FROM active_workflows aw, workflows w
             WHERE aw.workflow_id = w.id
               AND aw.country_code = $1
               AND aw.name = $2
        """
        val startTimeInMillis = System.currentTimeMillis()
        val params = listOf(
            countryCode,
            name
        )
        return database.get(GET_BY_KEY, params)
            .map {
                Workflow(it)
                    .activate()
            }
            .firstOrError()
            .doAfterTerminate {
                BackendRegistries.getDefaultNow().timer("fraud.rules.engine.activeWorkflowRepository.get")
                    .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }
}
