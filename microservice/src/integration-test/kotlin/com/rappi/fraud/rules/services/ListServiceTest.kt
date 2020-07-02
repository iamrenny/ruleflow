package com.rappi.fraud.rules.services

import com.rappi.fraud.rules.BaseTest
import com.rappi.fraud.rules.entities.BatchItemsRequest
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.repositories.ListHistoryRepository
import com.rappi.fraud.rules.repositories.ListRepository
import io.reactivex.Observable
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class ListServiceTest: BaseTest() {

    private val listRepository = injector.getInstance(ListRepository::class.java)
    private val listService =  injector.getInstance(ListService::class.java)
    private val database = injector.getInstance(Database::class.java)

    @BeforeEach
    fun configure() {
        clearTestLists()
    }

    @Test
    fun `test get list`(testContext: VertxTestContext) {

        listRepository.createList("list1Test", "list 1", "integrationTest").blockingGet()
        listRepository.createList("list2Test", "list 2", "integrationTest").blockingGet()

        listService.getLists().subscribe(
            { lists ->
                try {
                    assertEquals(2, lists.size)
                    assertEquals("list1Test", lists[0].listName)
                    assertEquals("list 1", lists[0].description)
                    assertEquals("integrationTest", lists[0].createdBy)
                    assertEquals("DISABLED", lists[0].status.name)
                    assertNotNull(lists[0].createdAt)
                    assertEquals("integrationTest", lists[0].lastUpdatedBy)
                    assertNotNull(lists[0].updatedAt)

                    assertEquals("list2Test", lists[1].listName)
                    assertEquals("list 2", lists[1].description)
                    assertEquals("integrationTest", lists[1].createdBy)
                    assertEquals("DISABLED", lists[1].status.name)
                    assertNotNull(lists[1].createdAt)
                    assertEquals("integrationTest", lists[1].lastUpdatedBy)
                    assertNotNull(lists[1].updatedAt)
                    testContext.completeNow()
                } catch (e: AssertionFailedError) {
                    testContext.failNow(e)
                }
            },{
                testContext.failNow(it)
            })
    }

    @Test
    fun `test create list`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "createListTest", description = "create list test", responsible = "integrationTest"
        ).blockingGet()

        try {
            assertNotNull(savedList.id)

            val resultList = listService.getList(savedList.id!!).blockingGet()

            assertEquals("createListTest", resultList.listName)
            assertEquals("create list test", resultList.description)
            assertEquals("DISABLED", resultList.status.name)
            assertEquals("integrationTest", resultList.createdBy)
            assertEquals("integrationTest", resultList.lastUpdatedBy)
            assertNotNull(resultList.createdAt)
            assertNotNull(resultList.updatedAt)

            testContext.completeNow()
        } catch (e: AssertionFailedError) {
            testContext.failNow(e)
        }
    }


    @Test
    fun `test find by name`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "findByNameListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        listService.getListByName("findByNameListTest")
            .subscribe({ list ->
                try {
                    assertEquals(savedList.id, list.id)
                    assertEquals("findByNameListTest", list.listName)
                    assertEquals("integrationTest", list.createdBy)
                    testContext.completeNow()
                } catch (e: AssertionFailedError) {
                    testContext.failNow(e)
                }
            },{
                testContext.failNow(it)
            })
    }

    @Test
    fun `add new item to list`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "addItemListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        val result = listService.addItem(savedList.id!!, "listItem1", "responsible1").blockingGet()

        assertNotNull(result)
        listService.getListItems(savedList.id!!).subscribe({
            list ->
                try {
                    assertTrue(list.isNotEmpty())
                    assertEquals(1, list.size)
                    assertEquals("listItem1", list[0])
                    testContext.completeNow()
                } catch (e: AssertionFailedError) {
                    testContext.failNow(e)
                }
            },{
                testContext.failNow(it)
            })
    }

    @Test
    fun `add existing item to list`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "addItemListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        val firstAddResponse = listService.addItem(savedList.id!!, "listItem1", "responsible1").blockingGet()
        val secondAddResponse = listService.addItem(savedList.id!!, "listItem1", "responsible2").blockingGet()

        assertEquals(firstAddResponse, secondAddResponse)
        listService.getListItems(savedList.id!!).subscribe({
            testContext.completeNow()
        }, {
            testContext.failNow(RuntimeException("Shouldn't fail because is an upsert"))
        })
    }

    @Test
    fun `delete item from list`(testContext: VertxTestContext) {

        val savedList = listService.createList(
            listName = "deteleItemListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        val item = listService.addItem(savedList.id!!, "listItem1", "responsible1").blockingGet()

        listService.removeItem(item.listId, item.value, "delete_responsible").blockingGet()

        listRepository.getItems(savedList.id!!).toList().subscribe ({ items ->
            assertTrue(items.none { it.listId == savedList.id!! && it.value == "listItem1"})
            assertTrue(items.isEmpty())
            testContext.completeNow()
        },
        {
            testContext.failNow(it)
        })
    }

    @Test
    fun `delete unexisting item from list`(testContext: VertxTestContext) {

        val savedList = listService.createList(
            listName = "deteleItemListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        listService.removeItem(savedList.id!!, "unexistingValue", "delete_responsible").subscribe({
                testContext.failNow(RuntimeException("should throw not found exception"))
            },
            {
                testContext.completeNow()
            })
    }

    @Test
    fun `get items by list name`(testContext: VertxTestContext) {
        val savedList1 = listService.createList(
            listName = "itemsByListNameTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()
        val savedList2 = listService.createList(
            listName = "otherListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()

        listService.addItem(savedList1.id!!, "listItem1", "responsible1").blockingGet()
        listService.addItem(savedList1.id!!, "listItem2", "responsible1").blockingGet()
        listService.addItem(savedList2.id!!, "listItem3", "responsible2").blockingGet()
        listService.addItem(savedList2.id!!, "listItem4", "responsible2").blockingGet()

        listService.getListItemsByListName("itemsByListNameTest").subscribe({
            items ->
            try {
                assertTrue(items.isNotEmpty())
                assertEquals(2, items.size)
                assertEquals("listItem1", items[0])
                assertEquals("listItem2", items[1])
                testContext.completeNow()
            } catch (e: AssertionFailedError) {
                testContext.failNow(e)
            }
        },{
            testContext.failNow(it)
        })
    }

    @Test
    fun `add masive items to list`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "batchItemsListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()
        listService.addItem(savedList.id!!, "previousValue", "itemResponsible").blockingGet()

        val request = BatchItemsRequest(
                        listOf(
                            "value1",
                            "value2",
                            "previousValue",
                            "value3",
                            "value4",
                            "value5",
                            "value6",
                            "value7",
                            "value8",
                            "value9",
                            "value10"),
                        "batchTestResponsible")
        try {
            listService.addItemsBatch(savedList.id!!, request).blockingGet()
            listService.getListItems(savedList.id!!).subscribe(
                { listItems ->
                    assertTrue(listItems.isNotEmpty())
                    assertEquals(11, listItems.size)
                    testContext.completeNow()
                },
                {
                    testContext.failNow(it)
                }
            )
        } catch (e: AssertionFailedError) {
            testContext.failNow(e)
        }
    }

    @Test
    fun `remove massive items from list with an unexistent item`(testContext: VertxTestContext) {
        val savedList = listService.createList(
            listName = "batchItemsListTest", description = "test list", responsible = "integrationTest"
        ).blockingGet()
        listService.addItem(savedList.id!!, "previousValue1", "itemResponsible").blockingGet()

        val request = BatchItemsRequest(
            listOf(
                "value1",
                "value2",
                "value3",
                "value4",
                "value5",
                "value6",
                "value7",
                "value8",
                "value9",
                "value10"),
            "batchTestResponsible")
        listService.addItemsBatch(savedList.id!!, request).blockingGet()

        val deleteRequest = request.copy(itemValues = request.itemValues + listOf("unexistingValue1"))

        try {
            listService.removeItemsBatch(savedList.id!!, deleteRequest).blockingGet()
            listService.getListItems(savedList.id!!).subscribe(
                { listItems ->
                    assertTrue(listItems.isNotEmpty())
                    assertEquals(1, listItems.size)
                    assertEquals("previousValue1", listItems.first())
                    testContext.completeNow()
                },
                {
                    testContext.failNow(it)
                }
            )
        } catch (e: AssertionFailedError) {
            testContext.failNow(e)
        }
    }

    private fun clearTestLists() {

        listRepository.getLists()
            .filter{ list -> list.listName.endsWith("Test")}
            .flatMap { testList ->
                database.executeDelete("DELETE from list_history WHERE list_id = $1", listOf(testList.id!!)).blockingGet()
                database.executeDelete("DELETE from list_items WHERE list_id = $1", listOf(testList.id!!)).blockingGet()
                database.executeDelete("DELETE from lists where id = $1", listOf(testList.id!!)).blockingGet()
                Observable.just(testList)
            }.toList().blockingGet()
        val lists = listRepository.getLists().toList().blockingGet()
        assertTrue(lists.isEmpty())
    }

}
