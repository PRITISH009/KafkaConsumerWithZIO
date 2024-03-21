package utils

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import com.typesafe.scalalogging.LazyLogging
import zio.{RIO, Task, ZIO, ZIOAppArgs}

import scala.io.Source

object ConfigUtils extends LazyLogging {
  case class Arg(key: String, value: String)

  def extractArg(arg: String): Arg = {
    val pattern = "-D(.*)=(.*)".r
    arg match {
      case pattern(key, value) => Arg(key, value)
    }
  }

  def createConfig(args: List[Arg]): Task[Config] = for {
    _ <- ZIO.logInfo("Creating Config")
    config <- ZIO.attempt {
      val emptyConfig: Config = ConfigFactory.empty()
      args.foldLeft(emptyConfig)((currentConfig: Config, nextArg: Arg) => {
        currentConfig.withValue(nextArg.key, ConfigValueFactory.fromAnyRef(nextArg.value))
      })
    }
  } yield(config)

  def readAllArguments(): RIO[ZIOAppArgs, List[Arg]] = for {
    _ <- ZIO.logInfo("Reading Program Arguments")
    extractedProgramArgs <- ZIOAppArgs.getArgs.map(_.toList)

    programArgs <- ZIO.attempt(extractedProgramArgs.map(extractArg))
    _ <- ZIO.logInfo(s"Got Program Arguments - [${programArgs.mkString(",")}]")

    systemArgs <- ZIO.attempt {
      sys.env.map {
        case (key: String, value: String) => Arg(key, value)
      }.toList
    }
    _ <- ZIO.logInfo(s"Got System Arguments - [${systemArgs.mkString(",")}]")

  } yield(programArgs ++ systemArgs)

  /** Load the contents of a file given an absolute path
   *
   * @param path
   *   An absolute Path
   * @return
   *   The contents of the file as a list of String, each element being one line
   */
  @SuppressWarnings(Array("org.wartremover.warts.PlatformDefault"))
  def loadSecret(path: String): List[String] = {
    logger.info(s"Trying to Access $path")
    val src = Source.fromFile(path)
    val res = src.getLines.toList
    src.close
    res
  }

}
