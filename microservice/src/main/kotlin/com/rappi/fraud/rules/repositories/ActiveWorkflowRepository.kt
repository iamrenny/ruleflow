package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Single
import io.vertx.micrometer.backends.BackendRegistries
import org.apache.commons.collections4.map.PassiveExpiringMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveWorkflowRepository @Inject constructor(private val database: Database) {

    val logger by LoggerDelegate()

    private var activeWorkflowCache = PassiveExpiringMap<String, Workflow>(90000)

    init {
        getActiveWorkflows()
            .ignoreElement()
            .subscribe({}, { logger.error("Unable to preload active workflows", it) })
    }

    fun save(workflow: Workflow, mustCache: Boolean = true): Single<Workflow> {

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
            .doOnSuccess { if(mustCache) activeWorkflowCache[it.cacheKey] = it }
    }

    fun get(countryCode: String, name: String): Single<Workflow> {
        val query = """
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

        val cached = activeWorkflowCache["workflow_${countryCode.toLowerCase()}_${name}"]

        return  if(cached == null) {
            database.get(query, params)
                .map(::Workflow)
                .firstOrError()
                .doOnSuccess(::cache)
                .doAfterTerminate {
                    BackendRegistries.getDefaultNow().timer("fraud.rules.engine.activeWorkflowRepository.get")
                        .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
                }
        } else {
            Single.just(cached)
        }
    }

    private fun getActiveWorkflows(): Single<Map<String, Workflow>> {
        val getActiveWorkflows = """
            SELECT w.id, w.country_code, w.name, w.version, w.workflow, w.user_id, w.created_at, true  
              FROM active_workflows aw, workflows w
             WHERE aw.workflow_id = w.id
             """
        return database.get(getActiveWorkflows, listOf())
            .map(::Workflow)
            .toMap { it.cacheKey }
            .doOnSuccess {
                activeWorkflowCache.clear()
                activeWorkflowCache.putAll(it)
            }
    }

    fun cache(workflow: Workflow) {
        activeWorkflowCache.put("workflow_${workflow.countryCode?.toLowerCase()}_${workflow.name}", workflow)
    }

    private val Workflow.cacheKey: String
            get() = "workflow_${this.countryCode?.toLowerCase()}_${this.name}"
}
