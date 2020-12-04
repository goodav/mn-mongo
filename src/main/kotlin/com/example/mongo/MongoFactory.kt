package com.example.mongo
import MongoConnectionPoolListener
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoDatabase
import io.micrometer.core.instrument.MeterRegistry
import io.micronaut.context.annotation.Factory
import org.litote.kmongo.KMongo
import javax.inject.Singleton

@Factory
class MongoFactory {

    @Singleton
    fun mongoConnectionPoolListener(meterRegistry: MeterRegistry): MongoConnectionPoolListener {
        return MongoConnectionPoolListener(meterRegistry)
    }

    @Singleton
    fun mongoClientOptionsBuilder(mongoConfiguration: MongoConfiguration, mongoConnectionPoolListener: MongoConnectionPoolListener): MongoClientOptions.Builder {
        return MongoClientOptions.builder().addConnectionPoolListener(mongoConnectionPoolListener)
    }

    @Singleton
    fun mongoClientURI(mongoConfiguration: MongoConfiguration, mongoClientOptionsBuilder: MongoClientOptions.Builder): MongoClientURI {
        // TODO: move away from one secret holding everything, and break out the config: mongodb://<user>:<pass>@<hosts>:<port>?<options>
        val mongoClientURI = MongoClientURI("${mongoConfiguration.uri}?${mongoConfiguration.options}", mongoClientOptionsBuilder)
        mongoClientURI.database ?: throw IllegalStateException("missing database name in your mongodb.uri")
        return mongoClientURI
    }

    @Singleton
    fun mongoClient(mongoClientURI: MongoClientURI): MongoClient {
        return KMongo.createClient(mongoClientURI)
    }

    @Singleton
    fun mongoDatabase(mongoClient: MongoClient, mongoClientURI: MongoClientURI): MongoDatabase {
        return mongoClient.getDatabase(mongoClientURI.database!!)
    }

}
