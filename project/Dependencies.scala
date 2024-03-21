import sbt._
import Versions._

object Dependencies {
  lazy val zioLibs: List[ModuleID] = List(
    "dev.zio"                %% "zio"                     % zioVersion,
    "dev.zio"                %% "zio-logging-slf4j"       % zioLogVersion,
    "dev.zio"                %% "zio-json"                % zioJsonVersion,
    "dev.zio"                %% "zio-config-typesafe"     % zioConfigVersion,
    "dev.zio"                %% "zio-kafka"               % zioKafkaVersion,
  )

  lazy val logLibs: List[ModuleID] = List(
    "ch.qos.logback"              % "logback-classic"     % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging"       % scalaLoggingVersion
  )

  lazy val configLibs: List[ModuleID] = List(
    "com.typesafe"          %  "config"        % typeSafeConfigVersion,
  )
}
