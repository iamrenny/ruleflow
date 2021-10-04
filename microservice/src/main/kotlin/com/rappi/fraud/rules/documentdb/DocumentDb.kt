package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.DocumentDbIndex
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.IndexOptions
import io.vertx.reactivex.ext.mongo.MongoClient

class DocumentDb @Inject constructor(
    val delegate: MongoClient
) {

    fun save(collection: String, json: JsonObject): Maybe<JsonObject> {
        return delegate
            .rxInsert(collection, json)
            .subscribeOn(Schedulers.io())
            .map {
                json.copy().put("_id", it)
            }
    }

    fun find(collection: String, query: JsonObject, options: FindOptions): Single<List<JsonObject>> {

        val mongoOptions = io.vertx.ext.mongo.FindOptions().apply {
            setLimit(options.limit)
            if (options.sort != null) {
                setSort(JsonObject().put(options.sort.fieldName, options.sort.sortOrder.order))
            }
        }
        return delegate
            .rxFindWithOptions(collection, query, mongoOptions)
            .subscribeOn(Schedulers.io())
    }

    /**
     * Indexes adminstration only needs to be done on the write instance. Since all changes are replicated to the read instance
     */
    fun listIndexes(collection: String): Observable<JsonObject> {
        return delegate.rxListIndexes(collection).toObservable().map {
            it.asIterable().map { it as JsonObject }
        }.flatMapIterable {
            it
        }.subscribeOn(Schedulers.io())
    }

    /**
     * Indexes adminstration only needs to be done on the write instance. Since all changes are replicated to the read instance
     */
    fun createIndexWithOptions(collection: String, documentDbIndex: DocumentDbIndex): Completable {
        val mongoIndexOptions = IndexOptions().apply {
            background(documentDbIndex.background)
            name(documentDbIndex.name)
        }
        val mongoIndexKey = JsonObject().put(documentDbIndex.key, 1)

        return delegate
            .rxCreateIndexWithOptions(collection, mongoIndexKey, mongoIndexOptions)
            .subscribeOn(Schedulers.io())
    }

    fun findBatch(collection: String, query: JsonObject, options: FindOptions): Single<List<JsonObject>> {

        val mongoOptions = io.vertx.ext.mongo.FindOptions().apply {
            batchSize = options.batch!!
            limit = options.limit
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
