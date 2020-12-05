package com.example.mongo

import com.example.mongo.internal.ObjectIdSerializationModule
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import mu.KotlinLogging
import javax.inject.Singleton


//@Singleton
class MicronautMongodbJacksonConfigurator : BeanCreatedEventListener<ObjectMapper?> {
    private val logger = KotlinLogging.logger { }
    override fun onCreated(event: BeanCreatedEvent<ObjectMapper?>): ObjectMapper {
        val mapper: ObjectMapper = event.bean ?: ObjectMapper()
        logger.info { "Adding ObjectId-to-string serializer for outgoing JSON responses..." }
        mapper.registerModule(ObjectIdSerializationModule())
        return mapper
    }
}
