package com.example.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.micronaut.context.annotation.Factory
import org.litote.kmongo.KMongo
import javax.inject.Singleton

@Factory
class MongoFactory {

  @Singleton
  fun meterRegistry(): MeterRegistry {
    return SimpleMeterRegistry()
  }

  @Singleton
  fun mongoConnectionPoolListener(meterRegistry: MeterRegistry): MongoConnectionPoolListener {
    return MongoConnectionPoolListener(meterRegistry)
  }


    @Singleton
    fun mongoClientSettingsBuilder(mongoConfiguration: MongoConfiguration, connectionString: ConnectionString, mongoConnectionPoolListener: com.example.mongo.MongoConnectionPoolListener): MongoClientSettings {
       return MongoClientSettings.builder()
           .applyConnectionString(connectionString)
            .applyToConnectionPoolSettings { builder ->
                builder.addConnectionPoolListener(
                    mongoConnectionPoolListener
                )
            }
           .build()
    }

    @Singleton
    fun connectionString(mongoConfiguration: MongoConfiguration) : ConnectionString {
        return ConnectionString("${mongoConfiguration.uri}?${mongoConfiguration.options}")
    }

    @Singleton
    fun mongoClient(mongoClientSettings: MongoClientSettings): MongoClient {
        return KMongo.createClient(mongoClientSettings)
    }

    @Singleton
    fun mongoDatabase(mongoClient: MongoClient, connectionString: ConnectionString,): MongoDatabase {
        return mongoClient.getDatabase(connectionString.database!!)
    }


}
