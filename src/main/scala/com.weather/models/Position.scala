package com.weather.models

import com.koddi.geocoder.Geocoder
import com.typesafe.scalalogging.LazyLogging
import com.weather.utils.{ConfigurationsApp, TruncateDoubleParametersTrait}
import play.api.libs.json.{Format, Json}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.Try


case class Position(lat: Double, lng: Double)
  extends TruncateDoubleParametersTrait[Position]
    with LazyLogging {

  override def truncateDoubles: Position = {
    Position(
      BigDecimal(lat).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
      BigDecimal(lng).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    )
  }

  override def toString: String = {
    s"$lat,$lng"
  }

  lazy val toEventualLocation: Future[Location] = {
    logger.debug(s"Capturing the name of the location from the position: lat=$lat, lng=$lng")
    val geo = Geocoder.createAsync(ConfigurationsApp.googleAPIKey)
    val eventualResults = geo.lookup(lat, lng)
    Try(
      eventualResults.map { results =>
        val positionName = {
          results
            .headOption
            .map(_.formattedAddress)
            .getOrElse(ConfigurationsApp.defaultUndefinedLocation)
        }
        logger.debug(s"Name of the position found as $positionName for lat=$lat, lng=$lng")
        Location(positionName)
      }
    ).fold(
      error => {
        logger.error(s"Error capturing the name for the position: lat=$lat," +
          s" lng=$lng as: $error setting to default name")
        Future.successful(Location(ConfigurationsApp.defaultUndefinedLocation))
      },
      eventualLocation => eventualLocation
    )
  }

}

/**
  * Want to ensure the locations are on the limits from Earth
  * reason to override the apply method
  */
object Position {
  def apply(lat: Double, lng: Double): Position = {
    (lat, lng) match {
      case (latitude, _) if latitude < ConfigurationsApp.minLatitude => Position(ConfigurationsApp.minLatitude, lng)
      case (latitude, _) if latitude > ConfigurationsApp.maxLatitude => Position(ConfigurationsApp.maxLatitude, lng)
      case (_, longitude) if longitude < ConfigurationsApp.minLongitude => new Position(lat, ConfigurationsApp.minLongitude)
      case (_, longitude) if longitude > ConfigurationsApp.maxLongitude => new Position(lat, ConfigurationsApp.maxLongitude)
      case _ => new Position(lat, lng)
    }
  }
}

trait PositionImplicitTrait {

  implicit val positionFormat: Format[Position] = Json.format[Position]
}