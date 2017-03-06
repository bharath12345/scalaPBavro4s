package com.glassbeam.bharath.experiments.producers

/**
  * Created by bharadwaj on 04/03/17.
  */

import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.glassbeam.bharath.experiments._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

class CakeProducer(config: Config) {

  private val producer = KafkaProducer(
    KafkaProducer.Conf(
      config,
      keySerializer = new StringSerializer,
      valueSerializer = new ByteArraySerializer)
  )

  private val topic = config.getString("topic")

  def submitSample(e: ScalarEvent) = producer.send(
    KafkaProducerRecord(topic, testPartition, e.toBinary())
  )

  def close() = producer.close()

}

object CakeProducer extends App {
  val config = ConfigFactory.load().getConfig("producer")
  val producer = new CakeProducer(config)
  producer.submitSample(e1)
  producer.submitSample(e2)
  producer.submitSample(e3)
  producer.close()
}
