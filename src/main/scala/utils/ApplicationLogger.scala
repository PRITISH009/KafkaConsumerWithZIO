package utils

import org.slf4j.event.Level
import org.slf4j.{Logger, LoggerFactory, Marker, MarkerFactory}
import zio.{Runtime, ZIOAppArgs, ZLayer}
import zio.logging.backend.SLF4J

trait ApplicationLogger {

  protected lazy val logger: Logger                          = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
  protected val zioSlf4jLogger: ZLayer[ZIOAppArgs, Any, Any] = Runtime.removeDefaultLoggers >>> SLF4J.slf4j
}
