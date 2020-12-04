package com.example.mongo.internal

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId

class ObjectIdSerializer : JsonSerializer<ObjectId>() {
    override fun serialize(value: ObjectId?, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value?.toHexString())
    }
}
