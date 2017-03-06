import Dependencies._

val slf4j = "1.7.24"
val logback = "1.2.1"
val scalaTest = "3.0.1"
val akka = "2.4.17"
val kafka = "0.10.2.0"
val typesafeConfig = "1.3.1"
val cake = "0.10.2.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.glassbeam",
      scalaVersion := "2.12.1",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "scalaPBavro4s",
    compileOrder := CompileOrder.JavaThenScala,
    resolvers += Resolver.bintrayRepo("cakesolutions", "maven"),
    libraryDependencies ++= Seq(
      "com.sksamuel.avro4s" %% "avro4s-core" % "1.6.4",
      "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf",
      "com.typesafe" % "config" % typesafeConfig,
      "com.typesafe.akka" %% "akka-slf4j" % akka,
      "org.apache.kafka" % "kafka-clients" % kafka,
      "ch.qos.logback" % "logback-classic" % logback,
      "org.slf4j" % "slf4j-api" % slf4j,
      "org.slf4j" % "log4j-over-slf4j" % slf4j,
      "net.cakesolutions" %% "scala-kafka-client" % cake,
      "net.cakesolutions" %% "scala-kafka-client-akka" % cake
    )
  )

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)