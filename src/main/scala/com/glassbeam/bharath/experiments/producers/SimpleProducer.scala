package com.glassbeam.bharath.experiments.producers

import java.util.{Properties, UUID}
import com.glassbeam.bharath.experiments._
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

/**
  * Created by bharadwaj on 05/03/17.
  */
object SimpleProducer extends App {

  private val props = new Properties()

  props.put("metadata.broker.list", "localhost:9092")
  props.put("message.send.max.retries", "5")
  props.put("request.required.acks", "-1")
  props.put("serializer.class", "kafka.serializer.DefaultEncoder")
  props.put("client.id", UUID.randomUUID().toString())

  private val producer = new KafkaProducer[String, Array[Byte]](props)
  val record = new ProducerRecord("CustomerCountry", "Precision Products", e1Bytes)
  try {
    producer.send(record)
  } catch {
    case e: Exception =>
    e.printStackTrace()
  }
}
