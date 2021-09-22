package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.DocumentDbIndex
import com.rappi.fraud.rules.entities.DocumentDbRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class DocumentDbInit @Inject constructor(
    private val documentDb: DocumentDb,
    private val documentDbRepos: @JvmSuppressWildcards Set<DocumentDbRepository>
) {

    private val logger by LoggerDelegate()

    fun createIndexes(): Completable {
        return Observable.fromIterable(documentDbRepos)
            .flatMapCompletable { repository ->
                createIndexesForRepository(repository)
            }
    }

    private fun createIndexesForRepository(repository: DocumentDbRepository): Completable {
        return getIndexesToApply(repository.collection, repository.indexes)
            .flatMapCompletable {
                createIndex(repository.collection, it)
            }
    }

    private fun getIndexesToApply(collectionName: String, collectionIndexes: Set<DocumentDbIndex>): Observable<DocumentDbIndex> {
        return getIndexesAppliedInDb(collectionName)
            .map { currentIndexes ->
                collectionIndexes.filter { !currentIndexes.contains(it.name) }
            }.flatMapObservable {
                Observable.fromIterable(it)
            }
    }

    private fun createIndex(collection: String, index: DocumentDbIndex): Completable {
        logger.info("CREATING INDEX ${index.name} FOR COLLECTION $collection")
        return documentDb.createIndexWithOptions(collection, index)
    }

    private fun getIndexesAppliedInDb(collection: String): Single<List<String>> {
        return documentDb.listIndexes(collection).map {
            logger.info("FOUND INDEX ${it.getString("name")} FOR COLLECTION $collection")
            it.getString("name")
        }.toList().cache()
    }
}
