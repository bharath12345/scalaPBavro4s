package com.glassbeam.bharath.experiments

/**
  * Created by bharadwaj on 04/03/17.
  */
class Producer extends App {

  val ve1 = VersionedEventOne("", "", "")
  val e1 = ScalarEvent("", 1, 1, ve1)

  val ve2 = VersionedEventTwo(0L, "", "", 0L)
  val e2 = ScalarEvent("", 2, 1, ve2)

  val ve3 = VersionedEventThree(Map())
  val e3 = ScalarEvent("", 3, 1, ve3)

}
