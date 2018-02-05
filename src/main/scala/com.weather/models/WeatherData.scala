package com.weather.models

import com.weather.models.Conditions.Condition
import org.joda.time.DateTime


case class WeatherData(
                        location: Location,
                        elevationPlusPosition: ElevationPlusPosition,
                        dateTime: DateTime,
                        condition: Condition,
                        temperature: Temperature,
                        pressure: Pressure,
                        humidity: Humidity
                      ) {
  override def toString: String = {
    s"${location.location}|${elevationPlusPosition.truncateDoubles}|$dateTime|$condition|${temperature.truncateDoubles}|" +
      s"${pressure.truncateDoubles}|${humidity.relativeHumidity}"
  }
}