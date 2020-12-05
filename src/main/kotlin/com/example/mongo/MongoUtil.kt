package com.example.mongo

import com.mongodb.ReadConcern
import com.mongodb.ReadConcernLevel
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import mu.KotlinLogging
import org.bson.Document
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

//@Singleton
class MongoUtil(
    private val mongoClient: MongoClient,
    private val mongoDatabase: MongoDatabase,
    private val MongoConfiguration: MongoConfiguration
) {

    private val logger = KotlinLogging.logger {}

    fun getMongoClient(): MongoClient {
        return mongoClient
    }

    fun getMongoDatabase(): MongoDatabase {
        return mongoDatabase
    }

    fun getMongoConfiguration(): MongoConfiguration {
        return MongoConfiguration
    }

    fun <T> getMongoCollection(collectionName: String, clazz: Class<T>): MongoCollection<T> {
        return getMongoCollection(collectionName, MongoConfiguration, clazz)
    }

    fun <T> getMongoCollection(collectionName: String, collectionConfiguration: MongoConfiguration.CollectionConfiguration, clazz: Class<T>): MongoCollection<T> {
        MongoConfiguration.collections.add(collectionConfiguration)
        return getMongoCollection(collectionName, MongoConfiguration, clazz)
    }

    fun <T> getMongoCollection(collectionName: String, MongoConfiguration: MongoConfiguration, clazz: Class<T>): MongoCollection<T> {

        logger.info("getting mongo collection: {}", collectionName)
        val collectionConfiguration = MongoConfiguration.getCollectionConfiguration(collectionName)

        val databaseWriteConcern = getWriteConcern(MongoConfiguration.write_concern)
        val databaseReadConcern = getReadConcern(MongoConfiguration.read_concern)

        val mongoCollection = mongoDatabase
            .withWriteConcern(databaseWriteConcern)
            .withReadConcern(databaseReadConcern)
            .getCollection(collectionName, clazz)
            .withWriteConcern(getWriteConcern(collectionConfiguration.write_concern, databaseWriteConcern))
            .withReadConcern(getReadConcern(collectionConfiguration.read_concern, databaseReadConcern))

        collectionConfiguration.drop_indexes.forEach {
            logger.info("dropping index (if exists): {}", it)
            for (doc in mongoCollection.listIndexes()) {
                if (doc.getString("name") == it) {
                    mongoCollection.dropIndex(it)
                    logger.warn("dropped index: {}", it)
                }
            }
        }

        collectionConfiguration.indexes.forEach {
            logger.info("ensuring index exists: {}", it.name)

            val mongoIndexOptions = IndexOptions()
                .unique(it.unique)
                .sparse(it.sparse)
                .background(it.background) // << this is actually ignored/deprecated in mongo 4.2+
                .name(it.name)

            val keys = if (it.ascending) Indexes.ascending(it.fields) else Indexes.descending(it.fields)

            if (!it.ttl_seconds.isEmpty) {
                mongoIndexOptions.expireAfter(it.ttl_seconds.get(), TimeUnit.SECONDS)
                if (it.fields.size != 1) {
                    throw RuntimeException("A TTL index must be a single field index!")
                }
            }

            val existingIndexes = getExistingIndexes(mongoCollection)

            val existingIndex = existingIndexes[it.name]
            if (existingIndex == null) {
                logger.info("index (${it.name}) does not exist so we will create it...")
                mongoCollection.createIndex(keys, mongoIndexOptions)
            } else {
                logger.info("index (${it.name}) already exists so we will check the config matches...")
                if (!it.ttl_seconds.isEmpty) {
                    validateTimeToLiveIndex(collectionName, it, existingIndex)
                } else if (!validateExistingIndex(it, existingIndex)) {
                    logger.warn("index (${it.name}) differs from existing (and recreatable is set to true), so dropping/creating...")
                    mongoCollection.dropIndex(it.name)
                    mongoCollection.createIndex(keys, mongoIndexOptions)
                }
            }
        }

        return mongoCollection
    }

    // if index exists and is a ttl index (special handling)
    private fun validateTimeToLiveIndex(
        collectionName: String,
        indexConfiguration: MongoConfiguration.CollectionConfiguration.IndexConfiguration,
        existingIndex: Document
    ) {

        val ttlSeconds = indexConfiguration.ttl_seconds.get()
        val currentExpireAfterSeconds = (existingIndex["expireAfterSeconds"].toString().toFloat().toLong()
            ?: throw RuntimeException("invalid index: ${indexConfiguration.name} - exists but is not a TTL index!"))

        if (currentExpireAfterSeconds != ttlSeconds) {
            logger.warn("ttl index already exists but current ttl (${currentExpireAfterSeconds} seconds) needs to be updated to $ttlSeconds")
            // ref: https://docs.mongodb.com/manual/reference/command/collMod/#dbcmd.collMod
            val collModCmd = Document.parse("{collMod: '${collectionName}', " +
                    " index : { keyPattern: {'${indexConfiguration.fields[0]}': 1}, " +
                    "           expireAfterSeconds: NumberLong(\"$ttlSeconds\") }}")
            val commandResult: Document = mongoDatabase.runCommand(collModCmd)
            val errorMessage = "something went wrong updating ttl index ${indexConfiguration.name}: $commandResult"
            if (commandResult.keys.containsAll(listOf("expireAfterSeconds_old", "expireAfterSeconds_new"))) {
                val expireAfterSecondsOld = commandResult["expireAfterSeconds_old"].toString().toFloat().toLong()
                val expireAfterSecondsNew = commandResult["expireAfterSeconds_new"].toString().toFloat().toLong()
                if (expireAfterSecondsOld != currentExpireAfterSeconds || expireAfterSecondsNew != ttlSeconds) {
                    throw RuntimeException(errorMessage)
                }
            } else {
                throw RuntimeException(errorMessage)
            }
        } else {
            logger.info("ttl index already exists and matches config! (${currentExpireAfterSeconds} seconds)")
        }
    }

    // we know the index exists (we need to confirm the index config matches existing index)
    private fun validateExistingIndex(
        indexConfiguration: MongoConfiguration.CollectionConfiguration.IndexConfiguration,
        existingIndex: Document
    ): Boolean {

        val errorMessages = mutableListOf<String>()

        val currentUniqueAttr = if (existingIndex["unique"] == null) false else existingIndex["unique"]
        if (currentUniqueAttr != indexConfiguration.unique) {
            errorMessages.add("existing index unique value mismatch (${currentUniqueAttr} != ${indexConfiguration.unique})")
        }

        val dir = (if (indexConfiguration.ascending) 1 else -1)
        val newFields = indexConfiguration.fields.map { it to dir }.toMap()
        val currentFields = (existingIndex["key"] as Document).toMap()
        if (currentFields != newFields) {
            errorMessages.add("existing index fields mismatch (${currentFields.keys} != ${indexConfiguration.fields})")
        }

        if (!errorMessages.isEmpty() && !indexConfiguration.recreatable) {
            errorMessages.forEach {
                logger.error("invalid index (${indexConfiguration.name}): $it")
            }
            throw RuntimeException("invalid index: ${indexConfiguration.name}")
        }

        return errorMessages.size == 0
    }

    private fun <T> getExistingIndexes(collection: MongoCollection<T>): Map<String, Document> {
        return collection.listIndexes().toList().map { it["name"].toString() to it }.toMap()
    }

    private fun getWriteConcern(writeConcernAsString: String): WriteConcern {
        return WriteConcern.valueOf(writeConcernAsString)
    }

    private fun getReadConcern(readConcernAsString: String): ReadConcern {
        return when (readConcernAsString) {
            "DEFAULT" -> ReadConcern.DEFAULT
            else -> ReadConcern(ReadConcernLevel.fromString(readConcernAsString))
        }
    }

    private fun getWriteConcern(writeConcernAsString: String?, defaultWriteConcern: WriteConcern): WriteConcern {
        return when (writeConcernAsString) {
            null -> defaultWriteConcern
            else -> getWriteConcern(writeConcernAsString)
        }
    }

    private fun getReadConcern(readConcernAsString: String?, defaultReadConcern: ReadConcern): ReadConcern {
        return when (readConcernAsString) {
            null -> defaultReadConcern
            else -> getReadConcern(readConcernAsString)
        }
    }
}
