package com.glassbeam.bharath

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.sksamuel.avro4s.{AvroBinaryOutputStream, AvroInputStream, AvroOutputStream, FromRecord, SchemaFor, ToRecord}

/**
  * Created by bharadwaj on 04/03/17.
  */
package object experiments {

  trait AvroFromBinary {
    def fromBinary[T: SchemaFor : FromRecord](bytes: Array[Byte]): T = {
      val in = new ByteArrayInputStream(bytes)
      val input = AvroInputStream.binary[T](in)
      input.iterator.toSeq.head
    }
  }

  trait AvroToBinary {
    def toBinary[T: SchemaFor : ToRecord](event: T): Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      val output = AvroOutputStream.binary[T](baos)
      output.write(event)
      output.close()
      baos.toByteArray
    }
  }

  // use the event_type and event_version to decode the event byte array
  case class ScalarEvent(emps: String, event_type: Int, event_version: Int, event: Array[Byte]) extends AvroToBinary {
    def toBinary(event: ScalarEvent): Array[Byte] = toBinary[ScalarEvent](event)
  }
  object ScalarEvent extends AvroFromBinary {
    def fromBinary(bytes: Array[Byte]): ScalarEvent = fromBinary[ScalarEvent](bytes)
  }

  case class VersionedEventOne(sysid1: String, sysid2: String, sysid3: String) extends AvroToBinary {
    def toBinary(event: VersionedEventOne): Array[Byte] = toBinary[VersionedEventOne](event)
  }
  object VersionedEventOne extends AvroFromBinary {
    def fromBinary(bytes: Array[Byte]): VersionedEventOne = fromBinary[VersionedEventOne](bytes)
  }

  case class VersionedEventTwo(obsts: Long, filename: String, bundleid: String, seenid: Long) extends AvroToBinary {
    def toBinary(event: VersionedEventTwo): Array[Byte] = toBinary[VersionedEventTwo](event)
  }
  object VersionedEventTwo extends AvroFromBinary {
    def fromBinary(bytes: Array[Byte]): VersionedEventTwo = fromBinary[VersionedEventTwo](bytes)
  }

  case class VersionedEventThree(kv: Map[String, String]) extends AvroToBinary {
    def toBinary(event: VersionedEventThree): Array[Byte] = toBinary[VersionedEventThree](event)
  }
  object VersionedEventThree extends AvroFromBinary {
    def fromBinary(bytes: Array[Byte]): VersionedEventThree = fromBinary[VersionedEventThree](bytes)
  }

  val ve1 = VersionedEventOne("sysid1", "sysid2", "sysid3")
  val e1 = ScalarEvent("e/m/p/s", 1, 1, ve1.toBinary())

  val ve2 = VersionedEventTwo(0L, "filename", "bundleid", 0L)
  val e2 = ScalarEvent("e/m/p/s", 2, 1, ve2.toBinary())

  val ve3 = VersionedEventThree(Map("key" -> "value"))
  val e3 = ScalarEvent("e/m/p/s", 3, 1, ve3.toBinary())

  val testTopic = "test"
  val testPartition = "test"
}
