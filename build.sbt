name := "akka-examples"

version := "1.0"

scalaVersion := "2.12.3"

val akkaVersion     = "2.5.6"
val akkaHttpVersion = "10.0.10"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-testkit"      % akkaVersion,
  "com.typesafe.akka"      %% "akka-http"         % akkaHttpVersion,
  "com.typesafe.akka"      %% "akka-http-testkit" % akkaHttpVersion,
  "de.heikoseeberger"      %% "akka-http-json4s"  % "1.18.0",
  "org.json4s"             %% "json4s-native"     % "3.5.3",
  "org.scala-lang.modules" %% "scala-async"       % "0.9.7",
  "org.scalatest"          %% "scalatest"         % "3.0.4" % "test"
)

