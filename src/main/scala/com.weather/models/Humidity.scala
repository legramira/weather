package com.weather.models

import com.weather.utils.{ConfigurationsApp, Randomiser}


case class Humidity(relativeHumidity: Int)

/**
  * Want to ensure the relative humidity be always in the range of 0 to 100 percentile
  * reason to override the apply method
  */
object Humidity extends HumidityTrait {
  def apply(relativeHumidity: Int): Humidity = {
    relativeHumidity match {
      case value if value < 0 => new Humidity(0)
      case value if value > 100 => new Humidity(100)
      case _ => new Humidity(relativeHumidity)
    }
  }
}


trait HumidityTrait {
  def obtainRandomHumidity(
                            newPressure: Pressure,
                            maybeCurrentPressure: Option[Pressure],
                            maybeCurrentHumidity: Option[Humidity]
                          ) = {
    val maybePreviousPressure = {
      for {
        currentPressure <- maybeCurrentPressure
        currentHumidity <- maybeCurrentHumidity
      } yield {
        val currentHumidityValue = currentHumidity.relativeHumidity
        val temporalMinRange = currentHumidityValue - ConfigurationsApp.humidityVariation
        val temporalMaxRange = currentHumidityValue + ConfigurationsApp.humidityVariation
        if (newPressure > currentPressure) {
          Randomiser.randomValueForLimits(temporalMinRange, currentHumidityValue)
        } else {
          Randomiser.randomValueForLimits(currentHumidityValue, temporalMaxRange)
        }
      }
    }

    Humidity(
      maybePreviousPressure.getOrElse(Randomiser.randomValueForLimits(0, 80)).toInt
    )
  }
}