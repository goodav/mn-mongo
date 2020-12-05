package com.example.app.service

import com.example.app.domain.Order
import com.example.app.repo.MongoRepository

class OrderService(
  //val mongoRepository: MongoRepository
) {
  fun getOrderById(orderId: Long) : Order? {
    return null //mongoRepository.findOrderById(orderId)
  }
}
