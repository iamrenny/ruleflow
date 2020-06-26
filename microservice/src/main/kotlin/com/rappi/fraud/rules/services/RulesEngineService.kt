package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.entities.WorkflowKey
import com.rappi.fraud.rules.parser.RuleEngine
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Maybe
import io.reactivex.Single

val workflows = mutableMapOf<String, RuleEngine>()

class RulesEngineService {
    private val logger by LoggerDelegate()

    fun get(key: WorkflowKey): Maybe<RuleEngine> {
        logger.info("Getting workflow in memory for ${buildKey(key)}")
        return exists(key)
            .filter { it }
            .flatMap {
                Maybe.just(workflows[buildKey(key)])
            }
    }

    fun set(key: WorkflowKey, entity: RuleEngine): Single<RuleEngine> {
        workflows[buildKey(key)] = entity
        logger.info("set workflow in memory for ${buildKey(key)}")
        logger.info("workflow memory size: ${workflows.size}")
        return Single.just(entity)
    }

    private fun exists(key: WorkflowKey): Single<Boolean> =
        Single.just(workflows).map {
            it.containsKey(buildKey(key))
        }

    private fun buildKey(key: WorkflowKey): String {
        return "rule_engine_${key.hashCode()}"
    }
}
