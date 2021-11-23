package com.rappi.fraud.rules.lists.cache

import com.google.inject.Inject
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.disposables.Disposable
import io.vertx.reactivex.redis.client.Command
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.Request
import io.vertx.reactivex.redis.client.Response

class EventsListener @Inject constructor(
    val redis: Redis,
    val listRepository: ListRepository
) {
    private val logger by LoggerDelegate()

    fun listen(): Disposable {
        return redis.rxConnect()
            .subscribe { connection ->
                logger.info("Succesfully connected to Redis for List Events Listener...")

                connection
                    .rxSend(Request.cmd(Command.SUBSCRIBE)
                    .arg("fraud_rules_engine_list_modifications"))
                    .subscribe()

                connection
                    .toObservable()
                    .filter { it.get(0).toString() == "message" }
                    .subscribe({ response ->
                        logger.info("List Events Listener Received response: $response")
                        val event = Event.fromResponse(response)
                        when (event.type) {
                            Event.Type.UPDATE -> listRepository.cachePut(event.listId)
                            Event.Type.DISABLED -> listRepository.cacheRemove(event.listName!!)
                            Event.Type.ENABLED -> listRepository.cachePut(event.listId)
                        }
                    }, {
                            logger.error("Error getting Redis response", it)
                        }, {
                            logger.info("Completed handler")
                        })
            }
    }

    private data class Event(val type: Type, val listId: Long, val listName: String? = null) {
        companion object {
            fun fromResponse(response: Response): Event {
                val message = response.get(2).toString().split(" ")
                return Event(Type.valueOf(message[0]), message[1].toLong(), message.getOrNull(2))
            }
        }
        enum class Type {
            UPDATE,
            ENABLED,
            DISABLED
        }
    }
}
