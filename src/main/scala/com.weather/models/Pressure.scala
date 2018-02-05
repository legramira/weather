package com.weather.models

import com.weather.utils.Randomiser
import com.weather.utils.{ConfigurationsApp, TruncateDoubleParametersTrait}


case class Pressure(millibars: Double)
  extends TruncateDoubleParametersTrait[Pressure]
    with Ordered[Pressure] {
  self =>

  override def truncateDoubles: Pressure = {
    Pressure(BigDecimal(millibars).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)
  }

  override def toString: String = {
    s"${self.truncateDoubles.millibars}mbars"
  }

  override def compare(that: Pressure): Int = (this.millibars - that.millibars).toInt
}

/**
  * Want to ensure the pressure stay in the ranges from the assumptions
  * reason to override the apply method
  */
object Pressure extends PressureTrait {
  def apply(millibars: Double): Pressure = {
    millibars match {
      case value if value < ConfigurationsApp.minLandPressure => new Pressure(ConfigurationsApp.minLandPressure)
      case value if value > ConfigurationsApp.maxLandPressure => new Pressure(ConfigurationsApp.maxLandPressure)
      case _ => new Pressure(millibars)
    }
  }
}

trait PressureTrait {

  def obtainRandomPressure(
                            elevationPlusPosition: ElevationPlusPosition,
                            newTemperature: Temperature,
                            maybeCurrentTemperature: Option[Temperature],
                            maybeCurrentPressure: Option[Pressure]
                          ): Pressure = {
    val decreasePressureDueAltitude = (elevationPlusPosition.elevation * ConfigurationsApp.variationPressurePerAltitude) / ConfigurationsApp.highestLandAltitude
    val initialMinRange = ConfigurationsApp.minLandPressure - decreasePressureDueAltitude
    val initialMaxRange = ConfigurationsApp.maxLandPressure - decreasePressureDueAltitude
    val maybePreviousPressure = {
      for {
        currentTemperature <- maybeCurrentTemperature
        currentPressure <- maybeCurrentPressure
      } yield {
        val currentPressureValue = currentPressure.millibars
        val temporalMinRange = currentPressureValue - ConfigurationsApp.variationAveragePressure
        val temporalMaxRange = currentPressureValue + ConfigurationsApp.variationAveragePressure
        if (temporalMinRange >= initialMinRange && temporalMaxRange <= initialMaxRange) {
          if (currentTemperature < newTemperature) {
            Randomiser.randomValueForLimits(temporalMinRange, currentPressureValue)
          } else {
            Randomiser.randomValueForLimits(currentPressureValue, temporalMaxRange)
          }
        } else {
          Randomiser.randomValueForLimits(initialMinRange, initialMaxRange)
        }
      }
    }

    Pressure(
      maybePreviousPressure.getOrElse(Randomiser.randomValueForLimits(initialMinRange, initialMaxRange))
    )
  }
}