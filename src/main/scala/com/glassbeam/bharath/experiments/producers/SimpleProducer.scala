package com.glassbeam.bharath.experiments.producers

import java.util.{Properties, UUID}
import com.glassbeam.bharath.experiments._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

/**
  * Created by bharadwaj on 05/03/17.
  */
object SimpleProducer extends App {

  private val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
  props.put("batch.size", "1")

  private val producer = new KafkaProducer[String, Array[Byte]](props)

  try {
    producer.send(new ProducerRecord(testTopic, testPartition, e1.toBinary()))
    producer.send(new ProducerRecord(testTopic, testPartition, e2.toBinary()))
    producer.send(new ProducerRecord(testTopic, testPartition, e3.toBinary()))
    println(s"Finished sending all records")
  } catch {
    case e: Exception =>
      e.printStackTrace()
  }
}
