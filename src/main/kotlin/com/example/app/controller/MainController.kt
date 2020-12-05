package com.example.app.controller

import com.example.app.service.OrderService
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller("/mongo/orders")
class MainController(
  val orderService: OrderService
) {

  @Get("/{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  suspend fun getOrder(@Parameter orderId: Long): Any? {
    return orderService.getOrderById(orderId)
  }
}
