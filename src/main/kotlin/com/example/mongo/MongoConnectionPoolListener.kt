package com.example.mongo

import io.micrometer.core.instrument.MeterRegistry

class MongoConnectionPoolListener(
  private val meterRegistry: MeterRegistry
) {//: ConnectionPoolListener {

//    private val poolSize: MutableMap<ServerId, AtomicInteger> = ConcurrentHashMap()
//    private val checkedOutCount: MutableMap<ServerId, AtomicInteger> = ConcurrentHashMap()
//    private val waitQueueSize: MutableMap<ServerId, AtomicInteger> = ConcurrentHashMap()
//    private val meters: MutableMap<ServerId, List<Meter>> = ConcurrentHashMap()
//
//    override fun connectionPoolCreated(event: ConnectionPoolCreatedEvent) {
//        val connectionMeters: MutableList<Meter> = ArrayList()
//        connectionMeters.add(registerGauge(event.serverId, METRIC_PREFIX + "size",
//            "the current size of the connection pool, including idle and and in-use members", poolSize))
//        connectionMeters.add(registerGauge(event.serverId, METRIC_PREFIX + "checkedout",
//            "the count of connections that are currently in use", checkedOutCount))
//        connectionMeters.add(registerGauge(event.serverId, METRIC_PREFIX + "waitqueuesize",
//            "the current size of the wait queue for a connection from the pool", waitQueueSize))
//        meters[event.serverId] = connectionMeters
//    }
//
//    override fun connectionPoolClosed(event: ConnectionPoolClosedEvent) {
//        val serverId = event.serverId
//        for (meter in meters[serverId]!!) {
//            meterRegistry.remove(meter)
//        }
//        meters.remove(serverId)
//        poolSize.remove(serverId)
//        checkedOutCount.remove(serverId)
//        waitQueueSize.remove(serverId)
//    }
//
//    override fun connectionCheckedOut(event: ConnectionCheckedOutEvent) {
//        checkedOutCount[event.connectionId.serverId]?.incrementAndGet()
//    }
//
//    override fun connectionCheckedIn(event: ConnectionCheckedInEvent) {
//        checkedOutCount[event.connectionId.serverId]?.decrementAndGet()
//    }
//
//    override fun connectionCreated(event: ConnectionCreatedEvent) {
//        poolSize[event.connectionId.serverId]?.incrementAndGet()
//    }
//
//    override fun connectionClosed(event:  ConnectionClosedEvent) {
//        poolSize[event.connectionId.serverId]?.decrementAndGet()
//    }
//
//    private fun registerGauge(serverId: ServerId, metricName: String, description: String, metrics: MutableMap<ServerId, AtomicInteger>): Gauge {
//        metrics[serverId] = AtomicInteger()
//        return Gauge.builder(metricName, metrics, { m: Map<ServerId, AtomicInteger> -> m.get(serverId)!!.toDouble() })
//            .tag("cluster.id", serverId.clusterId.value)
//            .tag("server.address", serverId.address.toString())
//            .register(meterRegistry)
//    }
//
//    open fun poolSize(): Int {
//        return if (!poolSize.values.isEmpty()) {
//            poolSize.values.map { it.get() }.sum()
//        } else 0
//    }
//
//    open fun checkedOutCount(): Int {
//        return if (!checkedOutCount.values.isEmpty()) {
//            checkedOutCount.values.map { it.get() }.sum()
//        } else 0
//    }
//
//    open fun waitQueueSize(): Int {
//        return if (!waitQueueSize.values.isEmpty()) {
//            waitQueueSize.values.map { it.get() }.sum()
//        } else 0
//    }
//
//    fun getMeterRegistry(): MeterRegistry {
//        return meterRegistry
//    }
//
//    override fun toString(): String {
//        return ("com.example.mongo.MongoConnectionPoolListener{" +
//                "poolSize='" + poolSize() + '\'' +
//                ", checkedOutCount='" + checkedOutCount() + '\'' +
//                ", waitQueueSize='" + waitQueueSize() + '\'' +
//                '}')
//    }
//
//    companion object {
//        private const val METRIC_PREFIX = "mongodb.driver.pool."
//    }
}
