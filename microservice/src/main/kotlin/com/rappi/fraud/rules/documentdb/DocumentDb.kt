package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.DocumentDbIndex
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.IndexOptions
import io.vertx.reactivex.ext.mongo.MongoClient
import org.msgpack.core.annotations.VisibleForTesting

class DocumentDb @Inject constructor(
    private val delegateWrite: MongoClient,
    private val delegateRead: MongoClient
) {

    fun save(collection: String, json: JsonObject): Maybe<JsonObject> {
        return delegateWrite.rxInsert(collection, json).map {
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
        return delegateRead.rxFindWithOptions(collection, query, mongoOptions)
    }

    /**
     * Indexes adminstration only needs to be done on the write instance. Since all changes are replicated to the read instance
     */
    fun listIndexes(collection: String): Observable<JsonObject> {
        return delegateWrite.rxListIndexes(collection).toObservable().map {
            it.asIterable().map { it as JsonObject }
        }.flatMapIterable {
            it
        }
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

        return delegateWrite.rxCreateIndexWithOptions(collection, mongoIndexKey, mongoIndexOptions)
    }

    fun findBatch(collection: String, query: JsonObject, options: FindOptions): Single<List<JsonObject>> {

        val mongoOptions = io.vertx.ext.mongo.FindOptions().apply {
            batchSize = options.batch!!
            limit = options.limit
            if (options.sort != null) {
                sort = JsonObject().put(options.sort.fieldName, options.sort.sortOrder.order)
            }
        }
        return delegateRead.rxFindWithOptions(collection, query, mongoOptions)
    }

    @VisibleForTesting
    fun getDelegate() = delegateWrite
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
