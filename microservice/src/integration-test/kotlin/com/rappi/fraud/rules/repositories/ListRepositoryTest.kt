package com.rappi.fraud.rules.repositories

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.ListStatus
import org.junit.jupiter.api.Test

class ListRepositoryTest : BaseTest() {

    private val repository = injector.getInstance(ListRepository::class.java)

    @Test
    fun testSaveLists() {


        repository.getListByName("1")

        repository.removeItem(2, "")

        repository.createList("2","","")

        repository.getList(2)

        repository.updateDescription(2, "","")

        repository.updateLastUpdatedBy(2, "")

        repository.updateStatus(2, ListStatus.DISABLED, "")

        repository.addItem(2, "")

        repository.getListByName("")

        repository.removeItemsBatch(2, listOf(""))

        repository.addItem(2, "")

        repository.addItemsBatch(2, listOf(""))


    }
}