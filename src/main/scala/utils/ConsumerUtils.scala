package utils

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import utils.ApplicationConstants.{Earliest, EmptyString, Ssl}
import utils.ConfigUtils.loadSecret
import zio.{ZLayer, durationInt}
import zio.kafka.consumer.{Consumer, ConsumerSettings}

import scala.util.{Failure, Success, Try}

object ConsumerUtils extends LazyLogging {

  def getConsumerSettings(config: Config): ConsumerSettings = {

    //val brokerRegionList: String = if(config.getString())loadSecret(config.getString(s"appSecrets.$brokerRegion")).mkString(",")

    val brokerList: List[String] = Try(config.getString("broker")) match {
      case Success(value) => List(value)
      case Failure(_) => {
        loadSecret("/etc/secrets/gcp_central_broker.txt")
      }
    }

//    val brokerList: List[String] = config.getString("brokerList").split(",").toList

    val autoOffsetReset: String = Try(config.getString("autoOffsetReset")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find autoOffsetReset value. Setting it as 'earliest' String")
        Earliest
      }
    }

    val securityProtocol: String = Try(config.getString("securityProtocol")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find securityProtocol value. Setting it as SSL String")
        Ssl
      }
    }

    val trustStoreLocation: String = Try(config.getString("truststoreLocation")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find truststoreLocation value. Setting it as Empty String")
        EmptyString
      }
    }

    val trustStorePassword: String = Try(config.getString("truststorePassword")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find truststorePassword value. Setting it as Empty String")
        EmptyString
      }
    }

    val keyStoreLocation: String = Try(config.getString("keystoreLocation")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find keystoreLocation value. Setting it as Empty String")
        EmptyString
      }
    }

    val keyStorePassword: String = Try(config.getString("keystorePassword")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find keystorePassword value. Setting it as Empty String")
        EmptyString
      }
    }

    val keyPassword: String = Try(config.getString("keyPassword")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find keyPassword value. Setting it as Empty String")
        EmptyString
      }
    }

    val endPointIdentificationAlgo: String = Try(config.getString("endpointIdentificationAlgorithm")) match {
      case Success(value: String) => value
      case Failure(exception) => {
        logger.warn("Didnt' Find endpointIdentificationAlgorithm value. Setting it as Empty String")
        EmptyString
      }
    }

    val groupId: String = Try(config.getString("groupId")) match {
      case Success(value: String) => value
      case Failure(exception) =>
        logger.warn("Didn't Find Group Id Value. Setting it as Empty String")
        "LOCAL_CONSUMER_GROUP"
    }

    ConsumerSettings(brokerList)
      .withPollInterval(2.seconds)
      .withProperty("auto.offset.reset", autoOffsetReset)
      .withProperty("security.protocol", securityProtocol)
      .withProperty("ssl.truststore.location", trustStoreLocation)
      .withProperty("ssl.truststore.password", trustStorePassword)
      .withProperty("ssl.keystore.location", keyStoreLocation)
      .withProperty("ssl.keystore.password", keyStorePassword)
      .withProperty("ssl.key.password", keyPassword)
      .withProperty("ssl.endpoint.identification.algorithm", endPointIdentificationAlgo)
      .withGroupId(groupId)
//      .withGroupId("error_retry_group")

  }

  def getConsumerLayer(consumerSettings: ConsumerSettings): ZLayer[Any, Throwable, Consumer] = ZLayer.scoped(
    Consumer.make(
      consumerSettings
    )
  )

}
