package com.glassbeam.bharath.experiments

import java.io.ByteArrayOutputStream

import com.sksamuel.avro4s.AvroOutputStream

object MessageSizeTest extends App {

  case class Ingredient(sugar: Long, fat: Double)
  case class Pizza(name: String, ingredients: Map[String, Ingredient], vegetarian: Boolean)

  val ing = Ingredient(10L, 10.0)
  val ings = Map("flour" -> ing, "something" -> ing)
  val pizza = Pizza("conizza", ings, true)

  val baosbinary = new ByteArrayOutputStream()
  val outputbinary = AvroOutputStream.binary[Pizza](baosbinary)
  outputbinary.write(pizza); outputbinary.close()
  println(s"Avro Binary conizza size: ${baosbinary.toByteArray.length}")

  val baosjson = new ByteArrayOutputStream()
  val outputjson = AvroOutputStream.json[Pizza](baosjson)
  outputjson.write(pizza); outputjson.close()
  println(s"Json conizza size: " + baosjson.toString("UTF-8").getBytes.length)

  val ing_p = com.glassbeam.message.Ingredient(10L, 10.0)
  val ings_p = scala.collection.immutable.Map("flour" -> ing_p, "something" -> ing_p)
  val pizza_p = com.glassbeam.message.Pizza("conizza", ings_p, true)

  val baosproto = new ByteArrayOutputStream()
  baosproto.write(pizza_p.toByteArray); baosproto.close()
  println(s"Protobuf conizza size: ${baosproto.toByteArray.length}")
  println(s"serializedSize = ${pizza_p.serializedSize}")
}

