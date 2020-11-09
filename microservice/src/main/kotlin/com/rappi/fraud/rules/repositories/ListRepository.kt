package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.ListItem
import com.rappi.fraud.rules.entities.ListStatus
import com.rappi.fraud.rules.entities.RulesEngineList
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.parser.errors.NotFoundException
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactiverse.reactivex.pgclient.Tuple
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.apache.commons.collections4.map.PassiveExpiringMap
import java.time.Duration
import java.time.LocalDateTime
import kotlin.streams.toList

class ListRepository @Inject constructor(private val database: Database) {

    private val logger by LoggerDelegate()
    private val listCache = PassiveExpiringMap<String, List<String>>(60000)


    init {
        findAll()
            .ignoreElement()
            .subscribe({},
                { logger.error("Unable to preload lists", it )}
            )
    }

    fun createList(listName: String, description: String, createdBy: String): Single<RulesEngineList> {
        val insert = """INSERT INTO lists (list_name, description, created_by, status, last_updated_by) 
                        VALUES ($1, $2, $3, $4, $5) 
                        RETURNING id, list_name, description, created_at, updated_at, created_by, last_updated_by, status"""

        val params = listOf(
            listName,
            description,
            createdBy,
            ListStatus.DISABLED.name,
            createdBy
        )

        return database.executeWithParams(insert, params)
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error creating list: $listName", it) }
    }

    fun getList(listId: Long): Single<RulesEngineList> {
        val query = """SELECT id, list_name, description, created_at, updated_at, created_by, last_updated_by, status 
            FROM lists WHERE id = $1"""

        return database.get(query, listOf(listId)).toList()
            .flatMap {
                if (it.isNotEmpty()) Single.just(it.first())
                else Single.error(NotFoundException("List not found", "error.not_found"))
            }
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error getting list with id: $listId", it) }
    }

    fun getListByName(listName: String): Single<RulesEngineList> {
        val query = """SELECT id, list_name, description, created_at, updated_at, created_by, last_updated_by, status 
            FROM lists WHERE list_name = $1"""

        return database.get(query, listOf(listName))
            .firstOrError()
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error getting list: $listName", it) }
    }

    fun getLists(): Observable<RulesEngineList> {
        val query = """SELECT id, list_name, description, created_at, updated_at, created_by, last_updated_by, status 
            FROM lists ORDER BY status desc, id"""
        return database.get(query, listOf())
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error getting lists", it) }
    }

    fun updateDescription(listId: Long, description: String, responsible: String): Single<RulesEngineList> {
        val params = listOf(description, responsible, listId)
        val query = """UPDATE lists SET description = $1, updated_at = NOW(), last_updated_by = $2 WHERE id = $3
            RETURNING id, list_name, description, created_at, updated_at, created_by, last_updated_by, status"""

        return database.executeWithParams(query, params)
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error updating description to list with id: $listId", it) }
    }

    fun updateStatus(listId: Long, status: ListStatus, responsible: String): Single<RulesEngineList> {
        val params = listOf(status.name, responsible, listId)
        val query = """UPDATE lists SET status = $1 , updated_at = NOW(), last_updated_by = $2 WHERE id = $3
            RETURNING id, list_name, description, created_at, updated_at, created_by, last_updated_by, status"""

        return database.executeWithParams(query, params)
            .map { RulesEngineList(it) }
            .doOnError { logger.error("error updating status to list with id: $listId", it) }
    }

    fun updateLastUpdatedBy(listId: Long, responsible: String): Completable {
        val params = listOf(responsible, listId)
        val query = """UPDATE lists SET updated_at = NOW(), last_updated_by = $1 WHERE id = $2
            RETURNING id, list_name, description, created_at, updated_at, created_by, last_updated_by, status"""

        return database.executeWithParams(query, params)
            .ignoreElement()
            .doOnError { logger.error("error updating lastUpdatedBy to list with id: $listId", it) }
    }

    fun addItem(listId: Long, itemValue: String): Single<ListItem> {
        val params = listOf(
            listId,
            itemValue)
        val query = """INSERT INTO list_items  (list_id, value) VALUES ($1, $2) 
               ON CONFLICT ON CONSTRAINT list_items_pk DO UPDATE SET value = EXCLUDED.value 
               RETURNING list_id, value"""

        return database.executeWithParams(query, params)
            .map {
                ListItem(listId, itemValue)
            }
            .doOnError {
                logger.error("error adding item to list with id: $listId", it)
            }
    }

    fun removeItem(listId: Long, itemValue: String): Completable {
        val params = listOf(
            listId,
            itemValue)
        val query = """DELETE FROM list_items WHERE list_id = $1 AND value = $2"""

        return database.executeDelete(query, params).flatMapCompletable {
            deletedItemsCount ->
            if (deletedItemsCount > 0) Completable.complete()
            else Completable.error(ErrorRequestException("Error deleting. Item not found", "error.not_found", 404))
        }
            .doOnError { logger.error("error removing item with value: $itemValue", it) }
    }

    fun getItems(listId: Long): Observable<ListItem> {

        val query = """SELECT list_id, value FROM list_items WHERE list_id = $1"""
        return database.get(query, listOf(listId))
            .map { ListItem(it) }
            .doOnError { logger.error("error getting list item for listId: $listId", it) }
    }

    fun getItemsByListName(listName: String): Observable<ListItem> {
        val query = """SELECT i.list_id, i.value FROM lists l JOIN list_items i on l.id = i.list_id WHERE l.list_name = $1"""
        return database.get(query, listOf(listName))
            .map { ListItem(it) }
            .doOnError { logger.error("error getting list item for listName: $listName", it) }
    }

    fun addItemsBatch(listId: Long, itemValues: List<String>): Completable {

        val query = """INSERT INTO list_items  (list_id, value) VALUES ($1, $2) 
               ON CONFLICT ON CONSTRAINT list_items_pk DO UPDATE SET value = EXCLUDED.value 
               RETURNING list_id, value"""

        val batchParams = itemValues.map { Tuple.of(listId, it) }

        return database.executeBatch(query, batchParams)
            .doOnError { logger.error("error adding batch items to listId: $listId", it) }
            .ignoreElement()
    }

    fun removeItemsBatch(listId: Long, itemValues: List<String>): Single<Int> {

        val query = """DELETE FROM list_items where list_id = $1 and value = $2"""
        val batchParams = itemValues.map { Tuple.of(listId, it) }

        return database.executeBatchDelete(query, batchParams)
            .doOnError { logger.error("error deleting batch items from listId: $listId", it) }
    }


    fun findAll(): Single<Map<String, List<String>>> = if(listCache.isNotEmpty())
        Single.just(listCache) else findAllWithEntriesWithoutCache()


    private fun findAllWithEntriesWithoutCache(): Single<Map<String, List<String>>> {
        val query = """SELECT list_name, id, value, description FROM lists JOIN list_items ON lists.id = list_items.list_id WHERE status = 'ENABLED'"""
        return database.get(query, listOf())
            .map { it ->
                Pair(it.getString("list_name"), it.getString("value"))
            }
            .toList()
            // TODO: SIMPLIFY ASAP
            .map { pair ->
                pair.groupBy { pair2 -> pair2.first }
                    .map { entry ->
                        Pair(entry.key,
                            entry.value.stream().map { it -> it.second }
                                .toList()
                        )
                    }.toMap()
            }
            .doOnSuccess {
                listCache.clear()
                listCache.putAll(it)
            }
            .doOnError { logger.error("error getting lists", it) }
    }
}