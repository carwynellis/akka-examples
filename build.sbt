name := "akka-examples"

version := "1.0"

scalaVersion := "2.12.5"

val akkaVersion     = "2.5.12"
val akkaHttpVersion = "10.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka"      %% "akka-testkit"      % akkaVersion,
  "com.typesafe.akka"      %% "akka-http"         % akkaHttpVersion,
  "com.typesafe.akka"      %% "akka-http-testkit" % akkaHttpVersion,
  "de.heikoseeberger"      %% "akka-http-json4s"  % "1.20.1",
  "org.json4s"             %% "json4s-native"     % "3.5.3",
  "org.scala-lang.modules" %% "scala-async"       % "0.9.7",
  "org.scalatest"          %% "scalatest"         % "3.0.4" % "test"
)

scalacOptions ++= Seq(
  "-target:jvm-1.8",        // Target Java 8
  "-explaintypes",          // Explain type errors with more detail
  "-deprecation",           // Emit deprecation warnings
  "-feature",               // Emit warnings where feature needs explicit import
  "-unchecked",             // Emit warnings related to type erasure
  "-Ywarn-unused:imports",  // Warn on unused imports
  "-Xfatal-warnings"        // Make warnings fatal
)

// Filter options that don't play well with the scala console.
// See https://tpolecat.github.io/2017/04/25/scalac-flags.html
scalacOptions in (Compile, console) ~= (_.filterNot(Set(
  "-Ywarn-unused:imports",
  "-Xfatal-warnings"
)))
