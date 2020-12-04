package com.example.mongo.internal

import com.fasterxml.jackson.databind.module.SimpleModule
import org.bson.types.ObjectId

class ObjectIdSerializationModule : SimpleModule() {
    init {
        addSerializer(ObjectId::class.java, ObjectIdSerializer())
    }
}
