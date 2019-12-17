package com.rappi.fraud.rules.services

import com.newrelic.api.agent.Trace
import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.redis.RedisClient
import java.time.Duration
import javax.inject.Inject

class CacheService @Inject constructor(private val redisClient: RedisClient) {

    private val logger by LoggerDelegate()

    @Trace(async = true)
    fun get(key: WorkflowKey): Maybe<RuleEngine> {
        logger.info("Getting value in cache for ${buildKey(key)}")
        return exists(key)
                .filter { it }
                .flatMap {
                    redisClient.rxGet(buildKey(key)).flatMap {
                        Maybe.just(JsonObject(it).mapTo(RuleEngine::class.java))
                    }
                }
    }

    @Trace(async = true)
    fun set(key: WorkflowKey, entity: RuleEngine): Completable {
        return redisClient
                .rxSetex(buildKey(key), Duration.ofMinutes(5).seconds, JsonObject.mapFrom(entity).toString())
                .ignoreElement()
    }

    @Trace(async = true)
    fun exists(key: WorkflowKey): Single<Boolean> {
        return redisClient.rxExists(buildKey(key)).map { it > 0 }
    }

    private fun buildKey(key: WorkflowKey): String {
        return "rule_engine_${key.hashCode()}"
    }
}
