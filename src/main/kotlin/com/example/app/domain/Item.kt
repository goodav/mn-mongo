package com.example.app.domain

import com.fasterxml.jackson.annotation.JsonFormat
import org.bson.types.ObjectId

data class Item(
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  val _id: ObjectId? = null,
  val itemId: Long,
  val description: String,
  val qty: Short
)
