package com.rappi.fraud.rules.services

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.rappi.fraud.rules.entities.ListHistory
import com.rappi.fraud.rules.entities.ListModificationType
import com.rappi.fraud.rules.entities.ListStatus
import com.rappi.fraud.rules.entities.RulesEngineList
import com.rappi.fraud.rules.repositories.ListHistoryRepository
import com.rappi.fraud.rules.repositories.ListRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import org.junit.jupiter.api.Test

class ListServiceUnitTest {
    val listRepository: ListRepository = mock()
    val listHistoryRepository: ListHistoryRepository = mock()
    val target: ListService = ListService(listRepository, listHistoryRepository)

    @Test
    fun `given a list when getting must return ok`() {

        whenever(listRepository.getList(1)).thenReturn(
            Single.just(RulesEngineList(
                id = 1,
                listName = "test",
                description = "test",
                status = ListStatus.ENABLED,
                createdBy = "me"
            )))

        target.getList(1)
            .test()
            .await()
            .assertNoErrors()
    }

    @Test
    fun `given a list when creating must return ok`() {

        whenever(listRepository.createList(eq("test"), eq("test"), eq("me"))).thenReturn(
            Single.just(RulesEngineList(
                id = 1,
                listName = "test",
                description = "test",
                status = ListStatus.ENABLED,
                createdBy = "me"
            )))
        whenever(listHistoryRepository.save(eq(1L), eq(ListModificationType.CREATE), eq("me"), any())).thenReturn(
            Single.just(ListHistory(1, 1, ListModificationType.CREATE.toString(), LocalDateTime.now(), "me", JsonObject())))

        target.createList("test", "test", "me")
            .test()
            .await()
            .assertNoErrors()
    }

    // TODO: This should not throw exception on invoking Grafana methods.
    @Test
    fun `given a list creation when error must return ok`() {

        whenever(listRepository.createList(eq("test"), eq("test"), eq("me"))).thenReturn(
            Single.just(RulesEngineList(
                id = 1,
                listName = "test",
                description = "test",
                status = ListStatus.ENABLED,
                createdBy = "me"
            )))

        whenever(listHistoryRepository.save(eq(1L), eq(ListModificationType.CREATE), eq("me"), any()))
            .thenReturn(Single.error(RuntimeException("Test")))

        target.createList("test", "test", "me")
            .test()
            .await()
            .assertNoErrors()
    }

    @Test
    fun `given list tracking update when error must return ok`() {

        whenever(listRepository.getList(1)).thenReturn(
            Single.just(RulesEngineList(
                id = 1,
                listName = "test",
                description = "test",
                status = ListStatus.ENABLED,
                createdBy = "me"
            )))

        whenever(listRepository.updateDescription(eq(1), eq("new desc"), eq("me"))).thenReturn(
            Single.just(RulesEngineList(
                id = 1,
                listName = "test",
                description = "test",
                status = ListStatus.ENABLED,
                createdBy = "me"
            )))
        whenever(listRepository.updateLastUpdatedBy(eq(1), eq("me"))).thenReturn(
            Completable.complete())

        whenever(listHistoryRepository.save(eq(1L), eq(ListModificationType.EDIT), eq("me"), any()))
            .thenReturn(Single.error(RuntimeException("Test")))

        target.updateDescription(1, "new desc", "me")
            .test()
            .await()
            .assertNoErrors()
    }
}
