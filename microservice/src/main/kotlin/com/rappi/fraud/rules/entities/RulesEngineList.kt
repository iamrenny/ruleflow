package com.rappi.fraud.rules.entities

import io.reactiverse.reactivex.pgclient.Row
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class RulesEngineList(
    val id: Long? = null,
    val listName: String,
    val description: String,
    val status: ListStatus = ListStatus.DISABLED,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val createdBy: String,
    val lastUpdatedBy: String? = null,
    val listItems: List<ListItem>? = null

) {

    constructor(row: Row) : this(
        id = row.getLong("id"),
        listName = row.getString("list_name"),
        description = row.getString("description"),
        status = ListStatus.valueOf(row.getString("status")),
        createdAt = row.getValue("created_at") as? LocalDateTime,
        updatedAt = row.getValue("updated_at") as? LocalDateTime,
        createdBy = row.getString("created_by"),
        lastUpdatedBy = row.getString("last_updated_by")
    )
}

enum class ListStatus {
    ENABLED, DISABLED;

    companion object {
        fun isValidValue(value: String) = values().any { it.name == value.toUpperCase() }
    }
}

enum class ListModificationType { CREATE, EDIT, CHANGE_STATUS, ADD_ITEMS, REMOVE_ITEMS, DELETE }

data class ListItem(val listId: Long, val value: String) {
    constructor(row: Row) : this(
        listId = row.getLong("list_id"),
        value = row.getString("value")
    )
}

data class ListHistory(
    val id: Long?,
    val listId: Long,
    val modificationType: String,
    val createdAt: LocalDateTime? = null,
    val responsible: String,
    val changeLog: JsonObject
) {
    constructor(row: Row) : this(
        id = row.getLong("id"),
        listId = row.getLong("list_id"),
        modificationType = row.getString("modification_type"),
        createdAt = row.getValue("created_at") as? LocalDateTime,
        responsible = row.getString("responsible"),
        changeLog = JsonObject(row.getJson("change_log").toString())
    )
}

data class BatchItemsRequest(val itemValues: List<String>, val responsible: String)
