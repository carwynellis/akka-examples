name := "getting-started-with-akka"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-xml-experimental" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.5.3",
  "org.json4s" %% "json4s-native" % "3.3.0"
)

