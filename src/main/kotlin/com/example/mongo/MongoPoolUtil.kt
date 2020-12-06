package com.example.mongo

import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class MongoPoolUtil(
  private val mongoConfiguration: MongoConfiguration,
  private val mongoConnectionPoolListener: com.example.mongo.MongoConnectionPoolListener
) {

  private val logger = KotlinLogging.logger {}

  fun isDatabaseConnectionPoolReady(): Boolean {
        val isPoolReady = mongoConnectionPoolListener.waitQueueSize() < mongoConfiguration.db_connection_pool_wait_threshold
        if (isPoolReady) {
            logger.debug("mongo connection pool readiness: {}", isPoolReady)
        } else {
            logger.warn("mongo connection pool readiness: {}", isPoolReady)
        }
        return isPoolReady
  }

}
