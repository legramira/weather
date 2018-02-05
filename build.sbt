name := "weather"

organization := "com.weather"

version := "0.1"

scalaVersion := "2.12.4"

val playWsStandaloneVersion = "1.1.3"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
libraryDependencies += "com.koddi" %% "geocoder" % "1.1.0"
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % playWsStandaloneVersion
libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % playWsStandaloneVersion
libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.2" % "test"
libraryDependencies += "org.specs2" %% "specs2-mock" % "4.0.2" % "test"