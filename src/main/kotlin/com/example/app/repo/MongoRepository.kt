package com.example.app.repo

import com.example.app.domain.Order
import com.example.app.domain.exception.PersistenceException

import com.mongodb.MongoWriteException
import com.mongodb.client.MongoCollection
import kotlinx.coroutines.CoroutineScope
import mu.KotlinLogging
import org.litote.kmongo.findOne
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.ReplaceOptions
import org.litote.kmongo.save

//@Singleton
class MongoRepository(
 // MongoUtil: MongoUtil,
  override val coroutineContext: CoroutineContext
) : CoroutineScope {

  private val logger = KotlinLogging.logger {}

  private val collectionName = "orders" // TODO find a better approach

 // var mongoCollection: MongoCollection<Order> = MongoUtil.getMongoCollection(collectionName, Order::class.java)

  fun findOrderById(orderId: Long): Order? {
   // return mongoCollection.findOne(eq(Order::orderId.name, orderId))
    return null
  }

//  fun getWorklistsByLocationIdAsync(locationId: Long): Deferred<List<Worklist>> {
//    return async {
//      mongoCollection.find(eq(Worklist::locationId.name, locationId))
//        .toList()
//    }
//  }


//  fun save(order: Order): Order {
//    if (order._id == null) {
//      try {
//        mongoCollection.save(order)
//      } catch (mongoWriteException: MongoWriteException) {
//        throw PersistenceException(
//          String.format("unable to save the document (Order) orderId:%d)!", order.orderId),
//          mongoWriteException)
//      }
//    }
//    else {
//      val updateResult = mongoCollection.replaceOne(
//        eq(Order::_id.name, order._id),
//        order,
//        ReplaceOptions().upsert(true)
//      )
//
//      // ensure the update was done
//      if (!updateResult.wasAcknowledged() || updateResult.modifiedCount != 1L || updateResult.matchedCount != 1L) {
//        throw PersistenceException(
//          String.format("unable to update the document (Order) orderId:%d)!", order.orderId)
//        )
//      }
//    }
//
//    return order
//  }

//  @Timed(value = "mongodb_requests", extraTags = ["method", "claimBatchesForWorklist"], percentiles = [0.50, 0.95, 0.99], histogram = true)
//  fun claimBatchesForWorklist(worklistID: String, pickupItems: List<PickupItem>, cartName: String, teamMemberId: String, teamMemberName: String, event: WorklistEvent, eventTimestamp: Date): Boolean {
//    return mongoCollection.updateOne(
//      eq(Worklist::worklistId.name, worklistID),
//      and(
//        setValue(Worklist::worklistId, worklistID),
//        setValue(Worklist::cartName, cartName),
//        setValue(Worklist::teamMemberId, teamMemberId),
//        setValue(Worklist::teamMemberName, teamMemberName),
//        setValue(Worklist::status, WorklistStatus.IN_PROGRESS),
//        setValue(Worklist::items, pickupItems),
//        setValue(Worklist::timestamps / WorklistTimestamps::worklistStartedTimestamp, eventTimestamp),
//        setValue(Worklist::timestamps / WorklistTimestamps::updatedTimestamp, eventTimestamp),
//        addToSet(Worklist::events, event)
//      )
//    ).wasAcknowledged()
//  }
}
