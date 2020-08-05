package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.entities.Workflow
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.micrometer.backends.BackendRegistries
import io.vertx.reactivex.redis.RedisClient
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkflowCache @Inject constructor(private val redisClient: RedisClient) {

    private val logger by LoggerDelegate()

    fun get(countryCode: String, name: String, version: Long? = null): Maybe<Workflow> {
        logger.info("Getting value in cache for ${buildKey(countryCode, name, version)}")
        val startTimeInMillis = System.currentTimeMillis()
        return exists(countryCode, name, version)
            .filter { exists -> exists }
            .flatMap {
                redisClient.rxGet(buildKey(countryCode, name, version)).flatMap {
                    Maybe.just(JsonObject(it).mapTo(Workflow::class.java).activate())
                }
            }.doAfterTerminate {
                BackendRegistries.getDefaultNow().timer("fraud.rules.engine.cacheService.get")
                    .record(System.currentTimeMillis() - startTimeInMillis, TimeUnit.MILLISECONDS)
            }
    }

    // TODO: This should return either the saved instance or a completable.
    fun set(entity: Workflow): Single<Workflow> {
        return redisClient
            .rxSetex(buildKey(entity), Duration.ofMinutes(5).seconds, JsonObject.mapFrom(entity).toString())
            .map {
                entity
            }
    }

    fun exists(countryCode: String, name: String, version: Long?): Single<Boolean> {
        return redisClient.rxExists(buildKey(countryCode, name, version)).map { it > 0 }
    }

    private fun buildKey(workflow: Workflow): String {
        return buildKey(workflow.countryCode!!, workflow.name, workflow.version)
    }

    private fun buildKey(countryCode: String, name: String, version: Long?): String {
        return if (version != null) {
            "rule_engine_${countryCode}_${name}_$version"
            } else {
            "rule_engine_${countryCode}_$name"
            }
        }
    }
