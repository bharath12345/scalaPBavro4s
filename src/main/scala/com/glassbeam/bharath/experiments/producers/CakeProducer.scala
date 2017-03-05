package com.glassbeam.bharath.experiments.producers

/**
  * Created by bharadwaj on 04/03/17.
  */

import com.typesafe.config.{Config, ConfigFactory}

class CakeProducer(config: Config) {

  /* private val producer = KafkaProducer(
    KafkaProducer.Conf(
      config,
      keySerializer = new StringSerializer,
      valueSerializer = new JsonSerializer[SubmitSampleCommand])
  )

  private val topic = config.getString("topic")

  def submitSample(meterId: MeterId, submitSampleCommand: SubmitSampleCommand) = producer.send(
    KafkaProducerRecord(topic, meterId.id.toString, submitSampleCommand)
  )

  def close() = producer.close() */

}

object CakeProducer extends App {

  val config = ConfigFactory.load().getConfig("producer")

}
