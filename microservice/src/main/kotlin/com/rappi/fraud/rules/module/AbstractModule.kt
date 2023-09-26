package com.rappi.fraud.rules.module

import com.codahale.metrics.MetricRegistry
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Named
import com.rappi.fraud.rules.db.JdbcClientProvider
import com.rappi.fraud.rules.documentdb.DocumentDb
import com.rappi.fraud.rules.documentdb.DocumentDbDataRepository
import com.rappi.fraud.rules.documentdb.DocumentDbProvider
import com.rappi.fraud.rules.entities.DocumentDbRepository
import com.rappi.fraud.rules.apm.SignalFxMetrics
import com.rappi.fraud.rules.repositories.Database
import com.rappi.fraud.rules.verticle.MainRouter
import com.rappi.fraud.rules.verticle.MainVerticle
import com.signalfx.codahale.SfxMetrics
import com.signalfx.codahale.reporter.SignalFxReporter
import com.signalfx.endpoint.SignalFxEndpoint
import com.signalfx.endpoint.SignalFxReceiverEndpoint
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.jdbc.JDBCClient
import io.vertx.reactivex.redis.RedisClient
import io.vertx.reactivex.redis.client.Redis
import io.vertx.redis.client.RedisOptions
import java.net.URL
import java.util.concurrent.TimeUnit

abstract class AbstractModule(private val vertx: Vertx, private val config: JsonObject) : AbstractModule() {

    override fun configure() {
        bind(Vertx::class.java).toInstance(vertx)
        bind(MainVerticle::class.java).asEagerSingleton()
        bind(MainRouter::class.java).asEagerSingleton()
        bind(JDBCClient::class.java)
            .toProvider(JdbcClientProvider::class.java)
            .asEagerSingleton()
        bind(DocumentDb::class.java).toProvider(DocumentDbProvider::class.java).asEagerSingleton()

        val documentDbRepositories = Multibinder.newSetBinder(binder(), DocumentDbRepository::class.java)
        documentDbRepositories.addBinding().to(DocumentDbDataRepository::class.java).asEagerSingleton()
    }

    @Provides
    fun documentDbProviderConfig(): DocumentDbProvider.Config {
        return config.getJsonObject("documentdb").let { documentDb ->
            DocumentDbProvider.Config(
                connectionStringWrite = documentDb.getString("CONNECTION_STRING_WRITE"),
                connectTimeout = documentDb.getString("CONNECT_TIMEOUT").toInt()
            )
        }
    }

    @Provides
    fun documentDbDataRepositoryConfig(): DocumentDbDataRepository.Config {
        return config.getJsonObject("documentdb").let { documentDb ->
            documentDb.getJsonObject("COLLECTIONS").let { collections ->
                DocumentDbDataRepository.Config(
                    collection = collections.getString("REQUEST_DATA"),
                    historyMonths = documentDb.getLong("LIMIT_HISTORY_MONTHS"),
                    collectionOld = collections.getString("REQUEST_DATA_OLD")
                )
            }
        }
    }

    @Provides
    fun dbConfig(): Database.Config {
        val db = config.getJsonObject("database")
        return Database.Config(
            url = db.getString("DB_URL"),
            user = db.getString("DB_USER"),
            pass = db.getString("DB_PASSWORD"),
            driver = db.getString("DRIVER_CLASS"),
            providerClass = db.getString("PROVIDER_CLASS"),
            connTimeout = db.getString("CONNECTION_TIMEOUT").toString().toLong(),
            leakDetectionThreshold = db.getString("LEAK_THRESHOLD").toString().toLong(),
            maxPoolSize = db.getString("MAX_POOL_SIZE").toString().toInt(),
            maxLifetime = db.getString("MAX_LIFETIME").toString().toInt(),
            minimumIdle = db.getString("MINIMUM_IDLE").toString().toInt(),
            idleTimeout = db.getString("IDLE_TIMEOUT").toString().toInt()
        )
    }

    @Provides
    fun database(db: Database.Config): Database {
        return Database(vertx, db)
    }

    @Provides
    fun redisClient(): RedisClient {
        val redisConfig = JsonObject()
            .put("host", config.getJsonObject("redis").getString("REDIS_HOST"))
            .put("port", config.getJsonObject("redis").getString("REDIS_PORT").toInt())
        return RedisClient.create(vertx, redisConfig)
    }

    @Provides
    fun redis(): Redis {

        val options = RedisOptions()
            .setConnectionString("redis://${
                config.getJsonObject("redis").getString("REDIS_HOST")
            }:${config.getJsonObject("redis").getString("REDIS_PORT").toInt()}")
        return Redis.createClient(
            vertx,
            options)
    }

    @Provides
    fun mainRouterConfig(): MainRouter.Config {
        return MainRouter.Config(
            timeout = config.getJsonObject("settings").getLong("APP_RESPONSE_TIMEOUT"),
            addRequestToken = config.getJsonObject("settings").getString("ADDED_REQUEST_AUTH_TOKEN")
        )
    }

    @Provides
    fun signalFxMetricsConfig(): SignalFxMetrics.Config {
        return SignalFxMetrics.Config(
            country = getCountry(),
            ingestUrl = config.getJsonObject("signal-fx").getString("SIGNALFX_INGEST_URL"),
            token = config.getJsonObject("signal-fx").getString("SIGNALFX_METRICS_AUTH_TOKEN"),
            timePeriodSeconds = 1500,
            featureGroups = config.getJsonArray("features").map { it.toString() }.toSet()
        )
    }

    @Provides
    fun sfxMetrics(config: SignalFxMetrics.Config): SfxMetrics {
        val ingestUrl = URL(config.ingestUrl)
        val signalFxEndpoint: SignalFxReceiverEndpoint =
            SignalFxEndpoint(ingestUrl.protocol, ingestUrl.host, ingestUrl.port)
        val metricRegistry = MetricRegistry()
        val signalFxReporter = SignalFxReporter.Builder(
            metricRegistry,
            config.token
        ).setEndpoint(signalFxEndpoint).build()
        signalFxReporter.start(config.timePeriodSeconds, TimeUnit.MILLISECONDS)
        return SfxMetrics(metricRegistry, signalFxReporter.metricMetadata)
    }
    @Provides
    @Named("countryCode")
    private fun getCountry() = config.getJsonObject("settings").getString("APP_COUNTRY_CODE")

}
