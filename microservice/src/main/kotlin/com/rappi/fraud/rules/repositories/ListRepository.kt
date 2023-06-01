package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.ListItem
import com.rappi.fraud.rules.entities.ListStatus
import com.rappi.fraud.rules.entities.RulesEngineList
import com.rappi.fraud.rules.exceptions.ErrorRequestException
import com.rappi.fraud.rules.exceptions.NotFoundException
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactiverse.reactivex.pgclient.Tuple
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.reactivex.redis.client.Command
import io.vertx.reactivex.redis.client.Redis
import io.vertx.reactivex.redis.client.Request

class ListRepository @Inject constructor(
    private val database: Database,
    private val redisClient: Redis
) {

    private val logger by LoggerDelegate()
    private val enabledListCache: MutableMap<String, Set<String>> = mutableMapOf()

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
            .doOnSuccess { publishStatus(status, it.id!!, it.listName) }
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
            .doOnSuccess { publishUpdate(it.listId) }
            .doOnError {
                logger.error("error adding item to list with id: $listId", it)
            }
    }

    fun removeItem(listId: Long, itemValue: String): Completable {
        val params = listOf(
            listId,
            itemValue)
        val query = """DELETE FROM list_items WHERE list_id = $1 AND value = $2"""

        return database.executeDelete(query, params)
            .flatMapCompletable {
                    deletedItemsCount ->
                if (deletedItemsCount > 0)
                    Completable.complete()
                else
                    Completable.error(ErrorRequestException("Error deleting. Item not found", "error.not_found", 404))
            }
            .doOnComplete { publishUpdate(listId) }
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
            .doOnSuccess { publishUpdate(listId) }
            .doOnError { logger.error("error adding batch items to listId: $listId", it) }
            .ignoreElement()
    }

    fun removeItemsBatch(listId: Long, itemValues: List<String>): Single<Int> {

        val query = """DELETE FROM list_items where list_id = $1 and value = $2"""
        val batchParams = itemValues.map { Tuple.of(listId, it) }

        return database
            .executeBatchDelete(query, batchParams)
            .doOnSuccess { publishUpdate(listId) }
            .doOnError { logger.error("error deleting batch items from listId: $listId", it) }
    }

    private fun publishUpdate(listId: Long) {
        redisClient.rxSend(Request.cmd(Command.PUBLISH).arg("fraud_rules_engine_list_modifications").arg("UPDATE $listId"))
            .subscribe({}, {
                logger.error(MSG_NOT_PUBLISH, it)
                SignalFx.noticeError(MSG_NOT_PUBLISH, it)
            }, {})
    }

    private fun publishStatus(status: ListStatus, listId: Long, listName: String) {
        val publishStatus = when (status) {
            ListStatus.ENABLED -> "ENABLED"
            ListStatus.DISABLED -> "DISABLED"
        }
        redisClient.rxSend(Request.cmd(Command.PUBLISH).arg("fraud_rules_engine_list_modifications").arg("$publishStatus $listId $listName"))
            .subscribe(
                {},
                {
                    logger.error(MSG_NOT_PUBLISH, it)
                    SignalFx.noticeError(MSG_NOT_PUBLISH, it)
                },
                {}
            )
    }

    private fun findAll(cache: Boolean = true): Single<MutableMap<String, MutableSet<String>>> {
        val query = """SELECT list_name, id, value, description, status, created_by FROM lists JOIN list_items ON lists.id = list_items.list_id WHERE status = 'ENABLED'"""
        return database.get(query, listOf())
            .collectInto(mutableMapOf<String, MutableSet<String>>()) {
                    acc, it ->

                val set = acc.getOrDefault(it.getString("list_name"), mutableSetOf())

                set.add(it.getString("value"))

                acc[it.getString("list_name")] = set
            }
            .doOnSuccess {
                if (cache) {
                    enabledListCache.clear()
                    enabledListCache.putAll(it)
                }
            }
            .doOnError { logger.error("error getting lists", it) }
    }

    fun getCached(): Map<String, Set<String>> {
        return enabledListCache
    }

    fun cachePut(listId: Long) {
        val query = """SELECT l.list_name, i.list_id, i.value FROM lists l JOIN list_items i on l.id = i.list_id WHERE l.id = $1 AND l.status = 'ENABLED'"""
        database.get(query, listOf(listId))
            .collectInto(mutableMapOf<String, MutableSet<String>>()) { acc, item ->
                val set = acc.getOrDefault(item.getString("list_name"), mutableSetOf())
                set.add(item.getString("value"))
                acc[item.getString("list_name")] = set
            }
            .subscribe({
                enabledListCache.putAll(it)
            }, {
                logger.error("error getting list item for list: $listId", it)
            })
    }

    fun cacheUpdateAll(): Completable {
        return findAll(true)
            .ignoreElement()
    }

    fun cacheRemove(listName: String) {
        enabledListCache.remove(listName)
    }

    companion object {
        const val MSG_NOT_PUBLISH = "Could not publish to Redis"
    }
}
