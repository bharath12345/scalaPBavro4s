package com.glassbeam.bharath.experiments.consumers

import java.util.{Collections, Properties}

import com.glassbeam.bharath.experiments._
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._
/**
  * Created by bharadwaj on 05/03/17.
  */
object SimpleConsumer extends App{

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "GroupOne")
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
        // unmarshall the object now
      }
    }
  } finally {
    consumer.close()
  }
}
