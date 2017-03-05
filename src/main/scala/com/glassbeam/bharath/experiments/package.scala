package com.glassbeam.bharath

import java.io.ByteArrayOutputStream

import com.sksamuel.avro4s.{AvroBinaryOutputStream, AvroOutputStream}

/**
  * Created by bharadwaj on 04/03/17.
  */
package object experiments {

  trait VersionedEvent extends Seq[Byte] {
    private var position = 0

    protected def bytes: Array[Byte]

    override def length: Int = bytes.length

    override def iterator: Iterator[Byte] = new Iterator[Byte] {
      override def hasNext: Boolean = position == length
      override def next(): Byte = { val byte = bytes(position); position += 1; byte }
    }

    override def apply(idx: Int): Byte = bytes(idx)
  }

  object VersionedEvent {
    implicit def versionedEventBytes(v: VersionedEvent): Array[Byte] = v.bytes

    def bytes[T](baos: ByteArrayOutputStream, abs: AvroBinaryOutputStream[T], t: T): Array[Byte] = {
      abs.write(t)
      abs.close()
      baos.toByteArray
    }

    def scalarEventBytes(e: ScalarEvent): Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      val binary = AvroOutputStream.binary[ScalarEvent](baos)
      VersionedEvent.bytes[ScalarEvent](baos, binary, e)
    }
  }

  // use the event_type and event_version to decode the event byte array
  case class ScalarEvent(emps: String, event_type: Short, event_version: Short, event: Array[Byte])

  case class VersionedEventOne(sysid1: String, sysid2: String, sysid3: String) extends VersionedEvent {
    override def bytes: Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      val binary: AvroBinaryOutputStream[VersionedEventOne] = AvroOutputStream.binary[VersionedEventOne](baos)
      VersionedEvent.bytes[VersionedEventOne](baos, binary, this)
    }
  }

  case class VersionedEventTwo(obsts: Long, filename: String, bundleid: String, seenid: Long) extends VersionedEvent {
    override def bytes: Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      val binary: AvroBinaryOutputStream[VersionedEventTwo] = AvroOutputStream.binary[VersionedEventTwo](baos)
      VersionedEvent.bytes[VersionedEventTwo](baos, binary, this)
    }
  }

  case class VersionedEventThree(kv: Map[String, String]) extends VersionedEvent {
    override def bytes: Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      val binary: AvroBinaryOutputStream[VersionedEventThree] = AvroOutputStream.binary[VersionedEventThree](baos)
      VersionedEvent.bytes[VersionedEventThree](baos, binary, this)
    }
  }

  val ve1 = VersionedEventOne("sysid1", "sysid2", "sysid3")
  val e1 = ScalarEvent("e/m/p/s", 1, 1, ve1)
  val e1Bytes = VersionedEvent.scalarEventBytes(e1)

  val ve2 = VersionedEventTwo(0L, "filename", "bundleid", 0L)
  val e2 = ScalarEvent("e/m/p/s", 2, 1, ve2)
  val e2Bytes = VersionedEvent.scalarEventBytes(e2)

  val ve3 = VersionedEventThree(Map("key" -> "value"))
  val e3 = ScalarEvent("e/m/p/s", 3, 1, ve3)
  val e3Bytes = VersionedEvent.scalarEventBytes(e3)

  val testTopic = "testTopic"
  val testPartition = "testPartition"
}
