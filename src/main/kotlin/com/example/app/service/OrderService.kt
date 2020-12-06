package com.example.app.service

import com.example.app.domain.Order
import com.example.app.repo.MongoRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class OrderService(
  val mongoRepository: MongoRepository//,
  //override val coroutineContext: CoroutineContext
) {//: CoroutineScope {
  suspend fun getOrderById(orderId: Long) : Order? {
    val order = mongoRepository.findOrderById(orderId)
    return order
  }
}
