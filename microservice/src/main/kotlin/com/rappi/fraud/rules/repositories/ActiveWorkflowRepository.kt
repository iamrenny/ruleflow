package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.Workflow
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveWorkflowRepository @Inject constructor(private val database: Database) {
    private val activeWorkFlowCache = ActiveWorkFlowCache(getActiveWorkflows())

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

    private fun getActiveWorkflows(): Single<Map<String, Workflow>> {
        val getActiveWorkflows = """
            SELECT w.id, w.country_code, w.name, w.version, w.workflow, w.user_id, w.created_at, true  
              FROM active_workflows aw, workflows w
             WHERE aw.workflow_id = w.id
             """
        return database.get(getActiveWorkflows, listOf()).map {
            Workflow(it)
        }.toMap {
            "rule_engine_${it.countryCode}_${it.name}"
        }
    }

    fun getActiveWorkflow(countryCode: String, name: String)
        = activeWorkFlowCache.get(countryCode, name)
}

class ActiveWorkFlowCache(private val source: Single<Map<String, Workflow>>) {

    private var activeWorkflows = source
    private var cacheUpdatedAt = LocalDateTime.now()
    private val cacheTtl = 900000

    fun get(countryCode: String, name: String): Single<Workflow> {
        val startTimeInMillis = System.currentTimeMillis()

        if(Duration.between(cacheUpdatedAt, LocalDateTime.now()).toMillis() > cacheTtl) {
            cacheUpdatedAt = LocalDateTime.now()
            activeWorkflows = source.cache()
        }

        return activeWorkflows.map {
            it["rule_engine_${countryCode}_${name}"]!!
        }.doFinally {
            BackendRegistries.getDefaultNow().timer("fraud.rules_engine.active_workflow_cache_time")
                .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
        }
    }
}

