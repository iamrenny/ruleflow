package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient

class DocumentDb @Inject constructor(
    val delegate: MongoClient
) {

    fun insert(collection: String, json: JsonObject): Completable {
        return delegate
            .rxInsert(collection, json)
            .subscribeOn(Schedulers.io())
            .ignoreElement()
    }

    fun find(collection: String, query: JsonObject): Maybe<JsonObject> {

        return delegate
            .rxFindOne(collection, query, JsonObject())
            .subscribeOn(Schedulers.io())
    }

    fun findBatch(collection: String, query: JsonObject, options: FindOptions): Single<List<JsonObject>> {

        val mongoOptions = io.vertx.ext.mongo.FindOptions().apply {
            batchSize = options.batch!!
            limit = if (options.limit > 0) options.limit else 0
            if (options.sort != null) {
                sort = JsonObject().put(options.sort.fieldName, options.sort.sortOrder.order)
            }
        }
        return delegate
            .rxFindWithOptions(collection, query, mongoOptions)
            .subscribeOn(Schedulers.io())
    }
}

data class FindOptions(
    val limit: Int = -1,
    val sort: SortOptions? = null,
    val batch: Int? = null
)

data class SortOptions(
    val fieldName: String,
    val sortOrder: SortOrder
)

enum class SortOrder(val order: Int) {
    ASCENDING(1),
    DESCENDING(-1);
}
