package com.rappi.fraud.rules.documentdb

import com.google.inject.Inject
import com.rappi.fraud.rules.entities.DocumentDbIndex
import com.rappi.fraud.rules.entities.DocumentDbRepository
import com.rappi.fraud.rules.entities.RiskDetail
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import com.rappi.fraud.rules.verticle.LoggerDelegate
import io.reactivex.Single
import io.vertx.core.json.JsonObject

data class EventData(
    val id: String? = null,
    val request: JsonObject,
    val response: JsonObject,
    val receivedAt: String? = null,
    val countryCode: String,
    val workflowName: String,
    val referenceId: String? = null
)

class DocumentDbDataRepository @Inject constructor(
    private val documentDb: DocumentDb,
    private val config: Config
) : DocumentDbRepository {

    override val collection: String
    override val indexes: Set<DocumentDbIndex>

    companion object {
        private const val ID = "_id"
        private const val REQUEST = "request"
        private const val RESPONSE = "response"
        private const val RECEIVED_AT = "received_at"
        private const val COUNTRY_CODE = "country_code"
        private const val WORKFLOW_NAME = "workflow_name"
        private const val REFERENCE_ID = "reference_id"
    }

    init {
        collection = config.collection
        indexes = setOf(
            DocumentDbIndex("received_at_index", RECEIVED_AT),
            DocumentDbIndex("country_code_index", COUNTRY_CODE),
            DocumentDbIndex("workflow_name_index", WORKFLOW_NAME),
            DocumentDbIndex("reference_id_index", REFERENCE_ID)
        )
    }

    private val logger by LoggerDelegate()

    fun saveEventData(eventData: EventData): Single<EventData> {
        val jsonToPersist = JsonObject().apply {
            put(REQUEST, eventData.request.toString())
            put(RESPONSE, eventData.response.toString())
            put(RECEIVED_AT, eventData.receivedAt)
            put(COUNTRY_CODE, eventData.countryCode)
            put(WORKFLOW_NAME, eventData.workflowName)
            put(REFERENCE_ID, findReferenceId(eventData))
        }

        return documentDb
            .save(config.collection, jsonToPersist)
            .map {
                EventData(
                    id = it.getString(ID),
                    request = JsonObject(it.getString(REQUEST)),
                    response = JsonObject(it.getString(RESPONSE)),
                    receivedAt = it.getString(RECEIVED_AT),
                    countryCode = it.getString(COUNTRY_CODE),
                    workflowName = it.getString(WORKFLOW_NAME),
                    referenceId = it.getString(REFERENCE_ID)
                )
            }.onErrorReturn {
                logger.error("$it")
                eventData
            }.toSingle()
    }

    fun find(requestId: String): Single<EventData> {
        val query = JsonObject().apply {
            put(ID, requestId)
        }

        val options = FindOptions(
            sort = SortOptions(ID, SortOrder.DESCENDING),
            limit = 1
        )

        return documentDb.find(config.collection, query, options)
            .map {
                if (it.isEmpty()) {
                    logger.error("Trying to find riskDetailData for $ID:$requestId but nothing was found")
                    throw NoRequestIdDataWasFound()
                }
                it.first()
            }.map {
                EventData(
                    id = it.getString(ID),
                    request = JsonObject(it.getString(REQUEST)),
                    response = JsonObject(it.getString(RESPONSE)),
                    receivedAt = it.getString(RECEIVED_AT),
                    countryCode = it.getString(COUNTRY_CODE),
                    workflowName = it.getString(WORKFLOW_NAME),
                    referenceId = it.getString(REFERENCE_ID)
                )
            }
    }

    fun getRiskDetailHistoryFromDocDb(
        request: RulesEngineHistoryRequest
    ): Single<List<RiskDetail>> {
        val batchSize = 500

        val options = FindOptions(
            batch = batchSize
        )

        val startQuery = "{"
        val receiveAtQuery = "\"${RECEIVED_AT}\" : { \"\$gt\" : \"${request.startDate}\", \"\$lt\" : \"${request.endDate}\"},"
        val workflowNameQuery = "\"${WORKFLOW_NAME}\" : { \"\$eq\" : \"${request.workflowName}\"},"
        val countryCodeQuery = "\"${COUNTRY_CODE}\" : { \"\$eq\" : \"${request.countryCode}\"}"
        val endQuery = "}"

        logger.info("Query: $receiveAtQuery$workflowNameQuery$countryCodeQuery")

        return documentDb.findBatch(
            config.collection,
            JsonObject(startQuery + receiveAtQuery + workflowNameQuery + countryCodeQuery + endQuery), options
        )
            .map {
                it.map {
                    RiskDetail(
                        id = it.getString(ID),
                        request = JsonObject(it.getString(REQUEST))
                    )
                }.toList()
            }
    }

    fun findInList(
        riskDetailIds: List<String>,
        workflowName: String? = null,
        countryCode: String? = null
    ): Single<List<RiskDetail>> {
        val batchSize = 500

        if (riskDetailIds.isEmpty()) return Single.just(emptyList())

        val options = FindOptions(
            batch = batchSize
        )

        val orderListAsString = riskDetailIds.map {
            "\"$it\""
        }.toList()

        return (riskDetailIds.windowed(riskDetailIds.size, batchSize, false))
            .map {
                logger.info("FindInValues docdb call size: ${it.size}")

                val referenceQuery = "\"reference_id\" : { \"\$in\" : $orderListAsString }"
                val workflowQuery = if (workflowName.isNullOrBlank()) "" else ", \"workflow_name\": { \"\$eq\" : \"$workflowName\" }"
                val countryCodeQuery = if (countryCode.isNullOrBlank()) "" else ", \"country_code\": { \"\$eq\" : \"$countryCode\" }"
                val query = JsonObject("{$referenceQuery$workflowQuery$countryCodeQuery}")
                documentDb.findBatch(config.collection, query, options)
            }.map { single ->
                single.map { json ->
                    json.map { response ->
                        RiskDetail(
                            id = response.getString(ID),
                            request = JsonObject(response.getString(REQUEST))
                        ) } }
            }.first()
    }

    fun findReferenceId(eventData: EventData): String {
        val entityType = findEntityType(eventData)
        return searchEntityId(entityType, requestToSearch(entityType, eventData.request))
    }

    private fun searchEntityId(entity: EntityType, request: JsonObject): String {
        val it = request.iterator()
        var entityId = ""

        while (it.hasNext() && entityId.isNullOrBlank()) {
            val next = it.next()
            if(next.key == entity.description) { entityId = next.value as String }
        }

        return entityId
    }

    private fun requestToSearch(entity: EntityType, request: JsonObject): JsonObject {
        return if (entity == EntityType.ORDER) {
            request.getJsonObject("order")
        } else {
            request
        }
    }

    private fun findEntityType(eventData: EventData): EntityType {
        val isStorekeeperEntityType = eventData.workflowName.startsWith("courier", true) ||
                eventData.workflowName.startsWith("handshake", true)

        return when {
            eventData.workflowName.contains("order") -> EntityType.ORDER
            isStorekeeperEntityType -> EntityType.STOREKEEPER
            else -> EntityType.USER
        }
    }

    data class Config(
        val collection: String
    )

    class NoRequestIdDataWasFound : RuntimeException()
}

enum class EntityType(val description: String? = null) {
    ORDER("order_id"),
    USER("user_id"),
    STOREKEEPER("storekeeper_id");

    override fun toString(): String {
        return name.toLowerCase()
    }
}
