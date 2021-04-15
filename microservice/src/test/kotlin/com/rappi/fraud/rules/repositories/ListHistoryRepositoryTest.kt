package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ListModificationType
import io.vertx.core.json.JsonObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ListHistoryRepositoryTest : BaseTest() {
    private val listRepository = injector.getInstance(ListRepository::class.java)
    private val historyRepository = injector.getInstance(ListHistoryRepository::class.java)

    @Test
    fun testListHistoryRepository() {

        listRepository.createList("aList", "aDescription", "anOwner")
            .flatMap {
                historyRepository.save(it.id!!, ListModificationType.ADD_ITEMS, "aResponsible", JsonObject())
            }
            .ignoreElement()
            .blockingAwait()

        historyRepository.getListHistory(1)
            .test()
            .await()
            .assertValue {
                Assertions.assertEquals(ListModificationType.ADD_ITEMS.name, it.modificationType)
                Assertions.assertEquals("aResponsible", it.responsible)
                true
            }
            .assertNoErrors()
            .assertComplete()
    }
}
