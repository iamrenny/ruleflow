package com.rappi.fraud.rules.lists.cache

import com.rappi.fraud.rules.repositories.ListRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.RedisConnection
import io.vertx.reactivex.redis.client.Response
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EventsListenerUnitTest {

    val redis: Redis = mockk()
    val listRepository: ListRepository = mockk()
    val redisConnection = mockk<RedisConnection>()
    val element = mockk<Response>(relaxed = true)

    lateinit var target: EventsListener

    @BeforeEach
    fun setup() {
        every { redisConnection.rxSend(any()) } returns Maybe.just(mockk())
        every { redisConnection.toObservable() } returns Observable.fromIterable(listOf(element))
        every { redis.rxConnect() } returns Single.just(redisConnection)
    }

    @Test
    fun `when instantiating eventsListener must go send SUBSCRIBE operation and observe responses`() {

        target = EventsListener(redis, listRepository)

        verify { redisConnection.rxSend(any()) }
        verify { redisConnection.toObservable() }
    }

    @Test
    fun `when receiving an update notification must put ok`() {
        every { element.get(0).toString() } returns "message"
        every { element.get(2).toString() } returns "UPDATE 15"
        every { redisConnection.toObservable() } returns Observable.fromIterable(listOf(element))
        every { redis.rxConnect() } returns Single.just(redisConnection)

        target = EventsListener(redis, listRepository)

        verify { listRepository.cachePut(15) }
    }

    @Test
    fun `when receiving an disabled notification must remove ok`() {

        every { element.get(0).toString() } returns "message"
        every { element.get(2).toString() } returns "DISABLED 15 test"

        every { redisConnection.toObservable() } returns Observable.fromIterable(listOf(element))

        target = EventsListener(redis, listRepository)

        verify { listRepository.cacheRemove("test") }
    }

    @Test
    fun `when receiving an enabled notification must enable ok`() {
        every { element.get(0).toString() } returns "message"
        every { element.get(2).toString() } returns "ENABLED 15 test"

        every { redisConnection.toObservable() } returns Observable.fromIterable(listOf(element))

        every { redis.rxConnect() } returns Single.just(redisConnection)

        target = EventsListener(redis, listRepository)

        verify { listRepository.cachePut(15) }
    }
}
