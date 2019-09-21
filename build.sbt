name := "AkkaTestTask"

version := "1.0"

scalaVersion := "2.13.1"

val akkaHttpVersion = "10.1.9"
val akkaStreamVersion = "2.5.23"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" %  akkaStreamVersion
)