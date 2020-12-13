package com.example.app.service

import com.example.app.domain.Item
import com.example.app.domain.Order
import com.example.app.domain.OrderType
import com.example.app.repo.MongoRepository
import kotlinx.coroutines.CoroutineScope
import java.util.*
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderService(
  val mongoRepository: MongoRepository//,
  //override val coroutineContext: CoroutineContext
) {//: CoroutineScope {
  suspend fun getOrderById(orderId: Long) : Order? {
    create()
    val order = mongoRepository.findOrderById(orderId)
    return order
  }

  suspend fun create() {
    var order = Order(
      orderId = System.currentTimeMillis(),
      firstName = "Hootie",
      lastName = "Blowfish",
      orderType = OrderType.Pickup("pickup"),
      createdDate = Date(),
      completedTimestamp = null,
      items = mutableListOf(
        Item(
          itemId = System.currentTimeMillis(),
          description = "foo",
          qty = 1
        )
      )
    )
    mongoRepository.save(order)
  }
}
