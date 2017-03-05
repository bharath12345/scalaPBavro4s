package com.glassbeam.bharath.experiments.consumers

import java.util.{Collections, Properties, UUID}

import com.glassbeam.bharath.experiments._
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import scala.annotation.switch
import scala.collection.JavaConverters._
/**
  * Created by bharadwaj on 05/03/17.
  */
object SimpleConsumer extends App{

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
  props.put(ConsumerConfig.CLIENT_ID_CONFIG, "your_client_id")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

  val consumer = new KafkaConsumer[String, Array[Byte]](props)
  consumer.subscribe(Collections.singletonList(testTopic))
  println(s"Consumer setup successfully")

  try {
    while (true) {
      val records = consumer.poll(100).asScala
      for (record <- records) {
        println(s"topic = ${record.topic()}, partition = ${record.partition()}, " +
          s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")

        val event: ScalarEvent = ScalarEvent.fromBinary(record.value())
        println(s"emps: ${event.emps}, type: ${event.event_type}, version: ${event.event_version}")

        (event.event_type: @switch) match {
          case 1 =>
            val eventOne = VersionedEventOne.fromBinary(event.event)
            println(s"Event One = $eventOne")
          case 2 =>
            val eventTwo = VersionedEventTwo.fromBinary(event.event)
            println(s"Event Two = $eventTwo")
          case 3 =>
            val eventThree = VersionedEventThree.fromBinary(event.event)
            println(s"Event Three = $eventThree")
          case x =>
            println(s"Unknown event type $x")
        }
      }
    }
  } finally {
    consumer.close()
  }
}
