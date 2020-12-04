package com.example.mongo

import io.micronaut.context.annotation.ConfigurationProperties
import java.util.Optional

//=========================================================================================
// NOTE: we are using snake case to match up with what we expect/want in the yaml config
//=========================================================================================

@ConfigurationProperties("mongodb")
class MongoConfiguration {
    var uri: String = "" // stop using this
    var username: String = "" // not used yet
    var password: String = "" // not used yet
    var hosts: String = "" // not used yet
    var port: String = "" // not used yet
    var options: String = "" // not used yet
    var collections = mutableListOf<CollectionConfiguration>()
    var db_connection_pool_wait_threshold: Int = 50
    var read_concern: String = "DEFAULT"
    var write_concern: String = "ACKNOWLEDGED"

    @ConfigurationProperties("collections")
    class CollectionConfiguration {
        var name: String = ""
        var drop_indexes: List<String> = emptyList()
        var indexes: List<IndexConfiguration> = emptyList()
        var read_concern: String? = null // will default to db level read concern
        var write_concern: String? = null // will default to db level write concern

        @ConfigurationProperties("indexes")
        class IndexConfiguration {
            var name: String = ""
            var unique: Boolean = false
            var sparse: Boolean = false
            var ascending: Boolean = true
            var ttl_seconds: Optional<Long> = Optional.empty()
            var fields: List<String> = emptyList()
            var recreatable: Boolean = false // if it exists but is different, drop and recreate
            var background: Boolean = false // TODO: needs to be implemented using async!
        }
    }

    fun getCollectionConfiguration(collectionName: String): CollectionConfiguration {
        return collections.find { it.name == collectionName }
            ?: throw UnsupportedOperationException("Unable to find configuration for collection \"$collectionName\", available configurations: " + collections.joinToString { it.name })
    }
}
