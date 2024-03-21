import com.typesafe.scalalogging.LazyLogging
import utils.ApplicationLogger
import utils.ConfigUtils.{createConfig, readAllArguments}
import utils.ConsumerUtils.{getConsumerLayer, getConsumerSettings}
import zio.kafka.consumer.{Consumer, Subscription}
import zio.kafka.serde.Serde
import zio.{RIO, ZIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

object KafkaConsumer extends ZIOAppDefault with LazyLogging {
//  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] = zioSlf4jLogger

  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  override def run: RIO[ZIOAppArgs, Unit] = for {
    _ <- ZIO.logInfo("Starting Consumer Job")
    args <- readAllArguments()
    config <- createConfig(args)

    _ <- ZIO.logInfo("Creating Consumer Settings")
    consumerSettings <- ZIO.attempt(getConsumerSettings(config))
    _ <- ZIO.logInfo(s"Consumer Settings - $consumerSettings")

    _ <- ZIO.logInfo("Creating Consumer Layer")
    consumerLayer = getConsumerLayer(consumerSettings)

    _ <- ZIO.logInfo("Consuming Messages")
    _ <- Consumer
      .plainStream(Subscription.topics(config.getString("topic")), Serde.int, Serde.string)
      .map(ele => {
        println(ele)
        ele
      })
      .map(_.offset)
      .mapZIO(offset => {
        println("Committing")
        offset.commit
      })
      .provideLayer(consumerLayer)
      .runDrain

  } yield ()
}
