package com.glassbeam.bharath.experiments.consumers

import org.apache.kafka.common.serialization.{ByteArraySerializer, Deserializer, StringDeserializer}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe, Unsubscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, KafkaConsumerActor}
import com.glassbeam.bharath.experiments.ScalarEvent
import com.typesafe.config.{Config, ConfigFactory}
import com.glassbeam.bharath.experiments.JavaDeserialiser
import com.glassbeam.bharath.experiments.consumers.CakeConsumerActor.EventDeserialiser
/**
  * Created by bharadwaj on 04/03/17.
  */

object CakeConsumerActor {

  def props(config: Config) = Props(new CakeConsumerActor(config))

  class EventDeserialiser extends JavaDeserialiser[ScalarEvent] {
    override def deserialize(topic: String, data: Array[Byte]): ScalarEvent = ScalarEvent.fromBinary(data)
  }

}

class CakeConsumerActor(config: Config) extends Actor with ActorLogging {

  private val extractor = ConsumerRecords.extractor[String, ScalarEvent]

  private val kafkaConsumerActor = context.actorOf(
    KafkaConsumerActor.props(
      consumerConf = KafkaConsumer.Conf(
        config,
        keyDeserializer = new StringDeserializer,
        valueDeserializer = new EventDeserialiser
      ),
      actorConf = KafkaConsumerActor.Conf(config),
      self
    ),
    "KafkaConsumer"
  )

  override def preStart() = {
    super.preStart()
    kafkaConsumerActor ! Subscribe
  }

  override def postStop() = {
    kafkaConsumerActor ! Unsubscribe
    super.postStop()
  }

  override def receive = {

    // extractor recovers the type parameters of ConsumerRecords, so pairs is of type Seq[(Option[String], SubmitSampleCommand)]
    case extractor(consumerRecords) =>

      consumerRecords.pairs.foreach {
        case (None, events) =>
          log.error(s"None partition, events: $events")

        case (Some(partition), events) =>
          log.info(s"received partition = $partition, events = $events")
      }

      // By committing *after* processing we get at-least-once-processing, but that's OK here because we can identify duplicates by their timestamps
      //kafkaConsumerActor ! Confirm(consumerRecords.offsets, commit = true)

  }
}

object CakeConsumer extends App {
  val config = ConfigFactory.load().getConfig("producer")
  val system = ActorSystem("CakeConsumer")
  val helloActor = system.actorOf(CakeConsumerActor.props(config), name = "CakeConsumer")
}
