package com.example.app.domain

import com.fasterxml.jackson.annotation.JsonFormat
import org.bson.types.ObjectId
import java.util.Date

data class Order(
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  val _id: ObjectId? = null,
  val orderId: Long,
  val firstName: String,
  val lastName: String,
  val orderType: OrderType?,
  val createdDate: Date,
  val completedTimestamp: Date?,
  val items: List<Item>
)
