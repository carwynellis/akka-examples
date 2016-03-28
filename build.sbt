name := "getting-started-with-akka"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)