package com.example.app.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(
    JsonSubTypes.Type(OrderType.Ship::class, name = "SHIP"),
    JsonSubTypes.Type(OrderType.Pickup::class, name = "PICKUP")
)
//@Serializable
sealed class OrderType {

    //@Serializable
    //@SerialName("SHIP")
    class Ship(val name: String) : OrderType()

    //@Serializable
    //@SerialName("PICKUP")
    class Pickup(val name: String) : OrderType()
}
