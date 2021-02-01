package com.rappi.fraud.rules.services

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.entities.BatchItemsRequest
import com.rappi.fraud.rules.entities.ListHistory
import com.rappi.fraud.rules.entities.ListItem
import com.rappi.fraud.rules.entities.ListModificationType
import com.rappi.fraud.rules.entities.ListStatus
import com.rappi.fraud.rules.entities.RulesEngineList
import com.rappi.fraud.rules.repositories.ListHistoryRepository
import com.rappi.fraud.rules.repositories.ListRepository
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class ListService @Inject constructor(
    private val listRepository: ListRepository,
    private val listHistoryRepository: ListHistoryRepository
) {

    private val logger by LoggerDelegate()

    fun createList(listName: String, description: String?, responsible: String): Single<RulesEngineList> {

        return listRepository.createList(listName, description ?: listName, responsible)
            .doOnSuccess {
                val changeLog = JsonObject.mapFrom(it)
                listHistoryRepository.save(it.id!!, ListModificationType.CREATE, it.createdBy, changeLog)
                    .subscribe({}, {
                        logger.error("error saving list history for list $listName", it)
                        SignalFx.noticeError(it)
                    })
            }
    }

    fun getList(listId: Long): Single<RulesEngineList> {
        return listRepository.getList(listId)
    }

    fun getListByName(listName: String): Single<RulesEngineList> {
        return listRepository.getListByName(listName)
    }

    fun getLists(): Single<List<RulesEngineList>> {
        return listRepository.getLists().toList()
    }

    fun updateDescription(listId: Long, description: String, responsible: String): Single<RulesEngineList> {
        return getList(listId)
            .flatMap {
                if (description != it.description)
                    listRepository.updateDescription(listId, description, responsible)
                        .doOnSuccess {
                            val changeLog = JsonObject().put("description", description)
                            updateTracking(listId, ListModificationType.EDIT, responsible, changeLog)
                        }
                else Single.just(it)
            }
    }

    fun updateStatus(listId: Long, status: ListStatus, responsible: String): Single<RulesEngineList> {
        return getList(listId)
            .flatMap {
                if (status != it.status)
                    listRepository.updateStatus(listId, status, responsible)
                        .doOnSuccess {
                            val changeLog = JsonObject().put("status", status.name)
                            updateTracking(
                                listId,
                                ListModificationType.CHANGE_STATUS,
                                responsible,
                                changeLog
                            )
                        }
                else Single.just(it)
            }
    }

    fun deleteList(listId: Long): Completable {
        // TODO definir si es borrado lógico o físico, dependiendo si se puede volver a crear otra lista con el mismo nombre
        return Completable.complete()
    }

    fun getListItems(listId: Long): Single<List<String>> {
        return listRepository.getItems(listId)
            .map { it.value }
            .toList()
    }

    fun getListItemsByListName(listName: String): Single<List<String>> {
        return listRepository.getItemsByListName(listName)
            .map { it.value }
            .toList()
    }

    fun addItem(listId: Long, itemValue: String, responsible: String): Single<ListItem> {
        return getList(listId)
            .flatMap {
                listRepository.addItem(listId, itemValue)
            }.doOnSuccess {
                val changeLog = JsonObject().put("items", JsonArray(listOf(itemValue)))
                updateTracking(listId, ListModificationType.ADD_ITEMS, responsible, changeLog)
            }.doOnError {
                logger.error("error adding item to list", it)
            }
    }

    fun removeItem(listId: Long, itemValue: String, responsible: String): Completable {
        return getList(listId)
            .flatMapCompletable {
                listRepository.removeItem(listId, itemValue)
            }.doOnComplete {
                val changeLog = JsonObject().put("items", JsonArray(listOf(itemValue)))
                updateTracking(listId, ListModificationType.REMOVE_ITEMS, responsible, changeLog)
            }
    }

    fun addItemsBatch(listId: Long, addItemsRequest: BatchItemsRequest): Completable {
        return getList(listId)
            .flatMapCompletable {
                listRepository.addItemsBatch(listId, addItemsRequest.itemValues)
            }.doOnComplete {
                val changeLog = JsonObject().put("items", JsonArray(addItemsRequest.itemValues))
                updateTracking(listId, ListModificationType.ADD_ITEMS, addItemsRequest.responsible, changeLog)
            }
    }

    fun removeItemsBatch(listId: Long, removeItemsRequest: BatchItemsRequest): Completable {
        return getList(listId)
            .flatMapCompletable {
                listRepository.removeItemsBatch(listId, removeItemsRequest.itemValues).ignoreElement()
            }.doOnComplete {
                val changeLog = JsonObject().put("items", JsonArray(removeItemsRequest.itemValues))
                updateTracking(listId, ListModificationType.REMOVE_ITEMS, removeItemsRequest.responsible, changeLog)
            }
    }

    private fun updateTracking(listId: Long, modificationType: ListModificationType, responsible: String, changeLog: JsonObject) {
        listRepository.updateLastUpdatedBy(listId, responsible)
            .subscribe({}, { logger.error("error updating lastUpdatedBy field", it) })
        listHistoryRepository.save(listId, modificationType, responsible, changeLog)
            .subscribe({}, {
                logger.error("error saving list history for listId $listId", it)
                SignalFx.noticeError(it)
            })
    }

    fun getListHistory(listId: Long): Single<List<ListHistory>> {
        return listHistoryRepository.getListHistory(listId)
            .toList()
            .doOnError {
                logger.error("error retrieving listHistory for listId: $listId", it)
            }
    }
}
