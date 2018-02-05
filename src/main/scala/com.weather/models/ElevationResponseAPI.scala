package com.weather.models

import play.api.libs.json.{Format, Json, Reads}
import com.weather.utils.TruncateDoubleParametersTrait



case class ElevationPlusPosition(
                                  elevation: Double,
                                  location: Position
                                ) extends TruncateDoubleParametersTrait[ElevationPlusPosition] { self =>
  override def truncateDoubles: ElevationPlusPosition = {
    self.copy(elevation = BigDecimal(self.elevation).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)
  }

  override def toString: String = {
    s"${location.truncateDoubles},$elevation"
  }
}

object ElevationPlusPosition {
  def apply(
             elevation: Double,
             location: Position
           ): ElevationPlusPosition = {
    elevation match {
      case elevationValue if elevationValue < 0 => new ElevationPlusPosition(0, location)
      case _ => new ElevationPlusPosition(elevation, location)
    }

  }
}


case class ElevationResponseAPI(results: Seq[ElevationPlusPosition])


object ElevationResponseAPIImplicits extends PositionImplicitTrait{

  implicit val elevationPlusPositionFormat: Format[ElevationPlusPosition] = Json.format[ElevationPlusPosition]
  implicit val elevationResponseAPIFormat: Reads[ElevationResponseAPI] = Json.reads[ElevationResponseAPI]
}