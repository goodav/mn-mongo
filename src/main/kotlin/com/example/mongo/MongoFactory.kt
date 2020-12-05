package com.example.mongo

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.micronaut.context.annotation.Factory
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
//
//    @Singleton
//    fun mongoClientSettingsBuilder(mongoConfiguration: MongoConfiguration, mongoConnectionPoolListener: com.example.mongo.MongoConnectionPoolListener): MongoClientSettings {
//       return MongoClientSettings.builder()
//           .applyConnectionString(ConnectionString("${mongoConfiguration.uri}?${mongoConfiguration.options}"))
//            .applyToConnectionPoolSettings { builder: Builder ->
//                builder.addConnectionPoolListener(
//                    mongoConnectionPoolListener
//                )
//            }
//           .build()
//    }
//
//    @Singleton
//    fun mongoClient(mongoClientSettings: MongoClientSettings): MongoClient {
//        return KMongo.createClient(mongoClientSettings)
//    }
////    @Singleton
////    fun mongoClientURI(mongoConfiguration: MongoConfiguration, mongoClientOptionsBuilder: MongoClientOptions.Builder): MongoClientURI {
////        // TODO: move away from one secret holding everything, and break out the config: mongodb://<user>:<pass>@<hosts>:<port>?<options>
////        val mongoClientURI = MongoClientURI("${mongoConfiguration.uri}?${mongoConfiguration.options}", mongoClientOptionsBuilder)
////        mongoClientURI.database ?: throw IllegalStateException("missing database name in your mongodb.uri")
////        return mongoClientURI
////    }
//
////    @Singleton
////    fun mongoClient(mongoClientURI: MongoClientURI): MongoClient {
////        return KMongo.createClient(mongoClientURI)
////    }
////
////    @Singleton
////    fun mongoDatabase(mongoClient: MongoClient, mongoClientURI: MongoClientURI): MongoDatabase {
////        return mongoClient.getDatabase(mongoClientURI.database!!)
////    }


}
