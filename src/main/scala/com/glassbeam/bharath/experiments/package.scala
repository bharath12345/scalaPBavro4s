package com.glassbeam.bharath

/**
  * Created by bharadwaj on 04/03/17.
  */
package object experiments {
  trait VersionedEvent
  case class ScalarEvent(emps: String, event_type: Short, event_version: Short, event: VersionedEvent)
  case class VersionedEventOne(sysid1: String, sysid2: String, sysid3: String) extends VersionedEvent
  case class VersionedEventTwo(obsts: Long, filename: String, bundleid: String, seenid: Long) extends VersionedEvent
  case class VersionedEventThree(kv: Map[String, String]) extends VersionedEvent
}
