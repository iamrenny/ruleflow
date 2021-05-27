package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.entities.ListStatus
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactiverse.reactivex.pgclient.Row
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.Redis
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ListRepositoryTest {

    val database: Database = mockk()
    val redisClient: Redis = mockk()

    private lateinit var repository: ListRepository

    @BeforeEach
    fun setup() {
        every { database.get(any(), any()) } returns Observable.empty()
        repository = ListRepository(database, redisClient)
        every { redisClient.rxSend(any()) } returns Maybe.just(mockk(relaxed = true))
    }

    @Test
    fun `given a batch add item  when updating must publish notification`() {

        every { database.executeBatch(any(), any()) } returns Single.just(1)

        repository.addItemsBatch(15, listOf("test1", "test2"))
            .test()
            .assertNoErrors()

        verify {
            redisClient.rxSend(any())
        }
    }

    @Test
    fun `given a batch remove item when updating must publish notification`() {

        every { database.executeBatchDelete(any(), any()) } returns Single.just(1)

        repository.removeItemsBatch(15, listOf("test1", "test2"))
            .test()
            .assertNoErrors()

        verify {
            redisClient.rxSend(any())
        }
    }

    @Test
    fun `given a remove item when updating must publish notification`() {

        every { database.executeDelete(any(), any()) } returns Single.just(1)

        repository.removeItem(15, "test1")
            .test()
            .assertNoErrors()

        verify {
            redisClient.rxSend(any())
        }
    }

    @Test
    fun `given an add item  when updating must publish notification`() {

        every { database.executeWithParams(any(), any()) } returns Single.just(mockk())

        repository.addItem(15, "test1")
            .test()
            .assertNoErrors()

        verify {
            redisClient.rxSend(any())
        }
    }

    @Test
    fun `given an update description when succeeding must publish notification`() {

        val row = mockk<Row>(relaxed = true)
        every { row.getString("status") } returns "ENABLED"

        every { database.executeWithParams(any(), any()) } returns Single.just(row)

        repository.updateStatus(15, ListStatus.ENABLED, "")
            .test()
            .assertNoErrors()

        verify {
            redisClient.rxSend(any())
        }
    }
}
