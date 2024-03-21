import Dependencies._

scalaVersion := "2.13.8"
version := "1.0"
name := "KafkaConsumerAndProducerZIO"

assembly / assemblyMergeStrategy := {
  case PathList("org", "apache", xs@_*) => MergeStrategy.first
  case PathList("com", "sun", xs@_*) => MergeStrategy.first
  case PathList("org", "glassfish", xs@_*) => MergeStrategy.first
  case PathList("javax", "inject", xs@_*) => MergeStrategy.first
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList("javax", "ws", xs@_*) => MergeStrategy.first
  case PathList("org", "aopalliance ", xs@_*) => MergeStrategy.first
  case PathList("META-INF", xs@_*) =>
    xs map {
      _.toLowerCase
    } match {
      case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
        MergeStrategy.discard
      case ps@x :: xs if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case "spring.schemas" :: Nil | "spring.handlers" :: Nil =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first
    }
  case "application.conf" => MergeStrategy.concat
  case "reference.conf" => MergeStrategy.concat

  case _ => MergeStrategy.first
}


libraryDependencies ++= zioLibs ++ logLibs ++ configLibs