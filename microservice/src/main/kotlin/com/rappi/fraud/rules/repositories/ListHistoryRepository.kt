package com.rappi.fraud.rules.repositories

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.ListHistory
import com.rappi.fraud.rules.entities.ListModificationType
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject

class ListHistoryRepository @Inject constructor(private val database: Database) {

    private val logger by LoggerDelegate()

    fun save(listId: Long, modificationType: ListModificationType, responsible: String, changeLog: JsonObject): Single<ListHistory> {
        val insert = """INSERT INTO list_history (list_id, modification_type, responsible, change_log) 
                        VALUES ($1, $2, $3, $4::JSON) 
                        RETURNING id, list_id, modification_type, created_at, responsible, change_log"""

        val params = listOf(
            listId,
            modificationType.name,
            responsible,
            changeLog
        )

        return database.executeWithParams(insert, params)
            .map(::ListHistory)
    }

    fun getListHistory(listId: Long): Observable<ListHistory> {
        val query = """SELECT id, list_Id, modification_type, created_at, responsible, change_log FROM list_history
            WHERE list_id = $1 ORDER BY id desc limit 50"""

        return database.get(query, listOf(listId))
            .map { ListHistory(it) }
            .doOnError { logger.error("error getting list with id: $listId", it) }
    }
}
