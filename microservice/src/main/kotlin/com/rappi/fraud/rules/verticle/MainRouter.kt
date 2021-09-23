package com.rappi.fraud.rules.verticle

import com.google.inject.Inject
import com.rappi.fraud.rules.apm.Grafana
import com.rappi.fraud.rules.apm.MetricHandler
import com.rappi.fraud.rules.apm.SignalFx
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.EventData
import com.rappi.fraud.rules.entities.ActivateRequest
import com.rappi.fraud.rules.entities.BatchItemsRequest
import com.rappi.fraud.rules.entities.CreateWorkflowRequest
import com.rappi.fraud.rules.entities.GetAllWorkflowRequest
import com.rappi.fraud.rules.entities.ListStatus
import com.rappi.fraud.rules.entities.RulesEngineHistoryRequest
import com.rappi.fraud.rules.entities.RulesEngineOrderListHistoryRequest
import com.rappi.fraud.rules.entities.UnlockWorkflowEditionRequest
import com.rappi.fraud.rules.errors.ErrorHandler
import com.rappi.fraud.rules.parser.errors.ErrorRequestException
import com.rappi.fraud.rules.parser.errors.NotFoundException
import com.rappi.fraud.rules.services.ListService
import com.rappi.fraud.rules.services.WorkflowService
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Single
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.LoggerHandler
import java.net.URLDecoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.reflect.KClass

class MainRouter @Inject constructor(
    private val vertx: Vertx,
    private val workflowService: WorkflowService,
    private val listService: ListService,
    private val documentDbDataRepository: DocumentDbDataRepository,
    val config: Config
) {

    private val logger by LoggerDelegate()

    companion object {
        const val HEADER_AUTH_USER = "X-Auth-User"
    }

    fun create(): Router {
        val router = Router
            .router(vertx)

        router.routeWithRegex("(?!/health-check).*").handler(LoggerHandler.create())
        router.routeWithRegex("(?!/health-check).*").handler(MetricHandler.create())

        router.route("/*").failureHandler(ErrorHandler())
        router.route().handler(BodyHandler.create())

        router.get("/health-check").handler { it.response().end("OK") }

        router.post("/workflow").handler(::createWorkflow)
        router.get("/workflow/active/:countryCode").handler(::getActiveWorkflowsByCountry)
        router.get("/workflow/:countryCode/:name/:version").handler(::getWorkflow)
        router.get("/workflow/:countryCode/:name").handler(::getWorkflowsByCountryAndName)
        router.get("/workflow/:countryCode").handler(::getAllWorkflowsByCountry)
        router.post("/workflow/:countryCode/:name/evaluate").handler(::evaluateActive)
        router.post("/workflow/:countryCode/:name/:version/evaluate").handler(::evaluate)
        router.post("/workflow/:countryCode/:name/:version/activate").handler(::activateWorkflow)
        router.post("/lists").handler(::createList)
        router.get("/lists").handler(::getLists)
        router.get("/lists/:list_id").handler(::getListById)
        router.delete("/lists/:list_id").handler(::deleteList)
        router.put("/lists/:list_id").handler(::updateList)
        router.put("/lists/:list_id/status").handler(::updateListStatus)
        router.post("/lists/:list_id/item").handler(::addItem)
        router.delete("/lists/:list_id/item").handler(::deleteItem)
        router.post("/lists/:list_id/items").handler(::addItemsBatch)
        router.delete("/lists/:list_id/items").handler(::deleteItemsBatch)
        router.get("/lists/:list_id/items").handler(::getListItems)
        router.get("/lists/:list_id/history").handler(::getListHistory)
        router.get("/user/:userId/workflow/:countryCode/:name/:version/edit").handler(::getWorkflowForEdition)
        router.put("/workflow/edit/cancel").handler(::cancelWorkflowEdition)
        router.get("/request-data/:requestId/data").handler(::getRequestData)
        router.get("/evaluation-history/:date_from/:date_to/:country/:workflow").handler(::getEvaluationHistory)
        router.post("/evaluation-history/request-history-order-list").handler(::getEvaluationOrderListHistory)
        router.post("/admin/save-request").handler(::saveRequest)

        return router
    }

    private fun saveRequest(ctx: RoutingContext) {
        validateAddedRequestAuthToken(ctx)
        documentDbDataRepository.saveEventData(buildEventDataFromContext(ctx.bodyAsJson)).subscribe({
            ctx.ok(JsonObject().put("request_id", it.id).toString())
        }, { error ->
            val message = "Error adding event_data ${ctx.bodyAsJson} into DocDB"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getRequestData(ctx: RoutingContext) {
        val requestId = ctx.request().getParam("requestId").toString()

        workflowService.getRequestIdData(requestId).subscribe({
            ctx.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encode(it).toString())
        }, { ex ->
            logger.error("Error retrieving risk detail data for $requestId", ex)
            ctx.fail(ex)
        })
    }

    private fun getAllWorkflowsByCountry(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).flatMap {
            validateCountry(it["countryCode"]!!)
            workflowService.getAllWorkflowsByCountry(it["countryCode"]!!)
                .toList()
        }.subscribe({
            ctx.ok(it.map { w ->
                JsonObject.mapFrom(w).toString()
            }.toString())
        }, { error ->
            val message = "Error getting workflows for '${ctx.pathParam("countryCode")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getActiveWorkflowsByCountry(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).flatMap {
            validateCountry(it["countryCode"]!!)
            workflowService.getActiveWorkflowsByCountry(it["countryCode"]!!)
                .toList()
        }.subscribe({
            ctx.ok(it.map { w ->
                JsonObject.mapFrom(w).toString()
            }.toString())
        }, { error ->
            val message = "Error getting active workflows for '${ctx.pathParam("countryCode")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun evaluateActive(ctx: RoutingContext) {
        validateCountry(ctx.pathParam("countryCode"))
        Single.just(ctx.bodyAsJson).flatMap {
            workflowService.evaluate(
                countryCode = ctx.pathParam("countryCode"),
                name = URLDecoder.decode(ctx.pathParam("name"), "UTF-8"),
                data = it
            ).timeout(config.timeout, TimeUnit.MILLISECONDS)
        }.subscribe({
                ctx.ok(JsonObject.mapFrom(it).toString())
        }, { cause ->
            logger.error("failed to evaluate workflow with request body ${ctx.bodyAsJson}", cause)
            Grafana.noticeError("failed to evaluate active workflow", cause)
            SignalFx.noticeError("failed to evaluate active workflow", cause)
            when (cause) {
                is NoSuchElementException -> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
                is TimeoutException -> ctx.response().setStatusCode(504).end()
                else -> ctx.fail(cause)
            }
        })
    }

    private fun evaluate(ctx: RoutingContext) {
        validateCountry(ctx.pathParam("countryCode"))
        Single.just(ctx.bodyAsJson).flatMap {
            workflowService.evaluate(
                ctx.pathParam("countryCode"),
                URLDecoder.decode(ctx.pathParam("name"), "UTF-8"),
                ctx.pathParam("version").toLong(), it, isSimulation(ctx)
            ).timeout(config.timeout, TimeUnit.MILLISECONDS)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { cause ->
            logger.error("failed to evaluate workflow with request body ${ctx.bodyAsJson}", cause)
            Grafana.noticeError("failed to evaluate workflow", cause)
            SignalFx.noticeError("failed to evaluate workflow", cause)
            when (cause) {
                is NotFoundException -> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
                is TimeoutException -> ctx.response().setStatusCode(504).end()
                else -> ctx.fail(cause)
            }
        })
    }

    private fun isSimulation(ctx: RoutingContext): Boolean {
        val runRulesParam = ctx.request().getParam("simulation")
        return runRulesParam == "true"
    }

    private fun createWorkflow(ctx: RoutingContext) {
        Single.just(ctx.bodyAsJson).map {
            validateCountry(it.getString("country_code"))
            CreateWorkflowRequest(
                countryCode = it.getString("country_code"),
                workflow = it.getString("workflow"),
                userId = ctx.getUserId()
            )
        }.flatMap {
            workflowService.save(it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error processing ${ctx.request().path()} for request ${ctx.bodyAsJson}"
            val ex = RuntimeException(message, error)
            logger.error(message, ex)
            ctx.fail(error)
        })
    }

    private fun getWorkflow(ctx: RoutingContext) {
        Single.just(ctx.pathParams())
            .flatMap {
                validateCountry(it["countryCode"]!!)
            workflowService.getWorkflow(
                it["countryCode"]!!,
                // TODO: FIX THIS ASAP. NAMES MUST BE URL COMPLIANT
                URLDecoder.decode(ctx.pathParam("name"), "UTF-8"),
                it["version"]!!.toLong()
            )
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error getting workflow '${ctx.pathParam("name")}' for '${ctx.pathParam("countryCode")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getWorkflowsByCountryAndName(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            validateCountry(it["countryCode"]!!)
            GetAllWorkflowRequest(
                countryCode = it["countryCode"]!!,
                name = URLDecoder.decode(it["name"]!!, "UTF-8")!!
            )
        }.flatMap {
            workflowService.getWorkflowsByCountryAndName(it).toList()
        }.subscribe({
            ctx.ok(it.map { w -> JsonObject.mapFrom(w).toString() }.toString())
        }, { error ->
            val message = "Error getting workflow '${ctx.pathParam("name")}' for '${ctx.pathParam("countryCode")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun activateWorkflow(ctx: RoutingContext) {
        Single.just(ctx.pathParams()).map {
            validateCountry(it["countryCode"]!!)
            ActivateRequest(
                countryCode = it["countryCode"]!!,
                name = URLDecoder.decode(it["name"]!!, "UTF-8")!!,
                version = it["version"]!!.toLong(),
                userId = ctx.getUserId()
            )
        }.flatMap {
            workflowService.activate(it)
        }.subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error activating workflow ${ctx.pathParam("name")} for '${ctx.pathParam("countryCode")}'"
            logger.error(message, error)
            Grafana.noticeError(message, error)
            SignalFx.noticeError(message, error)

            ctx.fail(error)
        })
    }

    private fun createList(ctx: RoutingContext) {
        val body = ctx.bodyAsJson
        val listName = body.getString("list_name") ?: throw ErrorRequestException("list_name required", "validation.body.bad_request", 400)
        val description = body.getString("description")
        val responsible = body.getString("responsible") ?: throw ErrorRequestException("responsible required", "validation.body.bad_request", 400)
        listService.createList(listName, description, responsible).subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error evaluating request ${ctx.bodyAsJson} "
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getLists(ctx: RoutingContext) {

        listService.getLists().subscribe({ lists ->
            ctx.ok(JsonObject().put("lists", lists.map { JsonObject.mapFrom(it) }).toString())
        }, { error ->
            val message = "Error evaluating request for listing items"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun deleteList(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        listService.deleteList(listId.toLong()).subscribe({
            ctx.ok("")
        }, { error ->
            val message = "Error deleting list '${ctx.pathParam("list_id")}' "
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getListById(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")

        listService.getList(listId.toLong()).subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error getting list '${ctx.pathParam("list_id")}' "
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun updateList(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        val body = ctx.bodyAsJson
        val description = body.getString("description") ?: throw ErrorRequestException("description required", "validation.body.bad_request", 400)
        val responsible = body.getString("responsible") ?: throw ErrorRequestException("responsible required", "validation.body.bad_request", 400)

        listService.updateDescription(listId.toLong(), description, responsible).subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error updating list '${ctx.pathParam("list_id")}' with request ${ctx.bodyAsJson}"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun updateListStatus(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        val body = ctx.bodyAsJson
        val statusParam = body.getString("status") ?: throw ErrorRequestException("description required", "validation.body.bad_request", 400)
        val status = if (ListStatus.isValidValue(statusParam.toUpperCase())) ListStatus.valueOf(statusParam.toUpperCase())
                    else throw ErrorRequestException("status is not valid", "validation.body.bad_request", 400)
        val responsible = body.getString("responsible") ?: throw ErrorRequestException("responsible required", "validation.body.bad_request", 400)

        listService.updateStatus(listId.toLong(), status, responsible).subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error updating list status '${ctx.pathParam("list_id")}' with request ${ctx.bodyAsJson}"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getListItems(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        listService.getListItems(listId.toLong()).subscribe({ items ->
            ctx.ok(JsonObject().put("items", items).toString())
        }, { error ->
            val message = "Error listing items from '${ctx.pathParam("list_id")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getListHistory(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        listService.getListHistory(listId.toLong()).subscribe({ listHistory ->
            ctx.ok(JsonObject().put("history", listHistory).toString())
        }, { error ->
            val message = "Error getting list '${ctx.pathParam("list_id")}' history"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun addItem(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        val body = ctx.bodyAsJson
        val itemValue = body.getString("item_value") ?: throw ErrorRequestException("item_value required", "validation.body.bad_request", 400)
        val responsible = body.getString("responsible") ?: throw ErrorRequestException("responsible required", "validation.body.bad_request", 400)

        listService.addItem(listId.toLong(), itemValue, responsible).subscribe({
            ctx.ok(JsonObject.mapFrom(it).toString())
        }, { error ->
            val message = "Error adding item ${ctx.bodyAsJson} to list '${ctx.pathParam("list_id")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun deleteItem(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")
        val body = ctx.bodyAsJson
        val itemValue = body.getString("item_value") ?: throw ErrorRequestException("item_value required", "validation.body.bad_request", 400)
        val responsible = body.getString("responsible") ?: throw ErrorRequestException("responsible required", "validation.body.bad_request", 400)

        listService.removeItem(listId.toLong(), itemValue, responsible).subscribe({
            ctx.ok("")
        }, { error ->
            val message = "Error deleting item ${ctx.bodyAsJson} from list '${ctx.pathParam("list_id")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun addItemsBatch(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")

        val addItemsRequest = ctx.bodyAs<BatchItemsRequest>(BatchItemsRequest::class)
        listService.addItemsBatch(listId.toLong(), addItemsRequest).subscribe({
            ctx.ok("")
        }, { error ->
            val message = "Error adding items ${ctx.bodyAsJson} to list '${ctx.pathParam("list_id")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun deleteItemsBatch(ctx: RoutingContext) {
        val listId = ctx.pathParam("list_id")

        val removeItemsRequest = ctx.bodyAs<BatchItemsRequest>(BatchItemsRequest::class)
        listService.removeItemsBatch(listId.toLong(), removeItemsRequest).subscribe({
            ctx.ok("")
        }, { error ->
            val message = "Error deleting items ${ctx.bodyAsJson} to list '${ctx.pathParam("list_id")}'"
            logger.error(message, error)
            ctx.fail(error)
        })
    }

    private fun getWorkflowForEdition(ctx: RoutingContext) {
        Single.just(ctx.pathParams())
            .flatMap {
                validateCountry(it["countryCode"]!!)
                workflowService.getWorkflowForEdition(
                    it["countryCode"]!!,
                    // TODO: FIX THIS ASAP. NAMES MUST BE URL COMPLIANT
                    URLDecoder.decode(ctx.pathParam("name"), "UTF-8"),
                    it["version"]!!.toLong(),
                    it["userId"]!!
                )
            }.subscribe({ ctx.response().end(Json.encode(it)) }, { err ->
                logger.error("Could not get workflow for edition: ${err.message}", err)
                ctx.fail(err)
            })
    }

    private fun cancelWorkflowEdition(ctx: RoutingContext) {
        val request = ctx.bodyAs<UnlockWorkflowEditionRequest>(UnlockWorkflowEditionRequest::class)

        workflowService.cancelWorkflowEdition(request)
            .subscribe({ ctx.response().end(Json.encode(it)) }, { err ->
            logger.error("Could not cancel workflow edition: ${err.message}", err)
            ctx.fail(err)
        })
    }

    private fun getEvaluationHistory(ctx: RoutingContext) {
        val dateFrom = ctx.request().getParam("date_from")
        val dateTo = ctx.request().getParam("date_to")
        val country = ctx.request().getParam("country")
        val workflow = ctx.request().getParam("workflow")

        val from = if (dateFrom.isEmpty()) LocalDateTime.now() else LocalDateTime.parse(dateFrom, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val to = if (dateTo.isEmpty()) LocalDateTime.now() else LocalDateTime.parse(dateTo, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        val request = RulesEngineHistoryRequest(from, to, workflow, country)

        workflowService.getEvaluationHistory(request).subscribe({
            ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(JsonObject().put("result", it).toString())
        }, { ex ->
            logger.error("Error processing /evaluation-history", ex)
            ctx.fail(ex)
        })
    }

    private fun getEvaluationOrderListHistory(ctx: RoutingContext) {
        var request = ctx.bodyAs<RulesEngineOrderListHistoryRequest>(RulesEngineOrderListHistoryRequest::class)

        val country = ctx.request().getParam("country")
        val workflow = ctx.request().getParam("workflow")

        request = request.copy(workflowName = workflow, countryCode = country?.toLowerCase())

        workflowService.getEvaluationOrderListHistory(request).subscribe({
            ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(JsonObject().put("result", it).toString())
        }, { ex ->
            logger.error("Error processing /evaluation-history", ex)
            ctx.fail(ex)
        })
    }

    private fun RoutingContext.getUserId(): String {
        return request().getHeader(HEADER_AUTH_USER)
            ?: throw RuntimeException("$HEADER_AUTH_USER is missing")
    }

    private fun RoutingContext.ok(chunk: String) =
        response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .putHeader("Content-Type", "application/json")
            .end(chunk)

    private fun <T> RoutingContext.bodyAs(clazz: KClass<out Any>): T {
        return try {
            Json.decodeValue(bodyAsString, clazz.java) as T
        } catch (exception: DecodeException) {
            logger.warn("Error parsing request Body for class ${clazz.simpleName}", exception)
            throw ErrorRequestException(exception.message ?: "Fail body validation", "validation.body.bad_request", 400)
        }
    }

    private fun validateCountry(country: String) {
        if (!country.matches("[a-z]+".toRegex())) {
            throw ErrorRequestException(country, "Invalid country code", 400)
        }
    }

    private fun buildEventDataFromContext(body: JsonObject): EventData {
        return EventData(
            "",
            body.getJsonObject("request"),
            body.getJsonObject("response"),
            body.getString("received_at"),
            body.getString("country_code"),
            body.getString("workflow_name")
        )
    }

    private fun validateAddedRequestAuthToken(ctx: RoutingContext) {
        val authToken = ctx.request().getHeader("X-Auth-Token")
        val authenticationKey = config.addRequestToken
        if (authToken != authenticationKey) {
            logger.warn("Unauthorized attempt to access to fraud-rules-engine save request to DocDB.")
            throw ErrorRequestException(
                "Unauthorized", "error.unauthorized",
                HttpResponseStatus.UNAUTHORIZED.code())
        }
    }

    data class Config(
        val timeout: Long,
        val addRequestToken: String
    )
}
