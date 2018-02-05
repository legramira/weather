package com.weather.models

import com.weather.utils.Randomiser
import org.joda.time.DateTime
import com.weather.utils.{ConfigurationsApp, TruncateDoubleParametersTrait}

case class Temperature(degreesCelsius: Double)
  extends TruncateDoubleParametersTrait[Temperature]
    with Ordered[Temperature]{

  override def truncateDoubles: Temperature = {
    Temperature(BigDecimal(degreesCelsius).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble)
  }

  override def toString: String = {
    val sign = if(degreesCelsius.signum == -1) "" else "+"
    s"$sign${degreesCelsius}°C"
  }

  override def compare(that: Temperature): Int = (this.degreesCelsius - that.degreesCelsius).toInt
}

object Temperature extends TemperatureTrait

trait TemperatureTrait {
  def obtainRandomTemperature(
                               elevationPlusPosition: ElevationPlusPosition,
                               dateTime: DateTime,
                               maybeCurrentTemperature: Option[Temperature] = None
                             ): Temperature = {
    /** Modify the min or max temperature range depending of day/night cycle*/
    def modifyRangeDependingHourOfDay(minRange: Double, maxRange: Double) = {
      val hour = dateTime.hourOfDay().get
      if (hour > 6 && hour < 18) {
        (minRange, maxRange + ConfigurationsApp.dailyVariation)
      } else {
        (minRange - ConfigurationsApp.dailyVariation, maxRange)
      }
    }

    /** Modify the min or max temperature range depending of time of the year (seasons)*/
    def modifyRangeDependingOfSeason(minRange: Double, maxRange: Double, latitude: Double) = {
      val monthOfTheYear = dateTime.monthOfYear().get
      val isMidYear = monthOfTheYear > 3 && monthOfTheYear < 9
      (isMidYear, latitude) match {
        case (true, lat) if lat.signum < 0 => (minRange - ConfigurationsApp.seasonalVariation, maxRange - ConfigurationsApp.seasonalVariation)
        case (true, _) => (minRange + ConfigurationsApp.seasonalVariation, maxRange + ConfigurationsApp.seasonalVariation)
        case (_, lat) if lat.signum < 0 => (minRange + ConfigurationsApp.seasonalVariation, maxRange + ConfigurationsApp.seasonalVariation)
        case _ => (minRange - ConfigurationsApp.seasonalVariation, maxRange - ConfigurationsApp.seasonalVariation)
      }
    }

    /** Modify the min or max temperature range depending of the altitude of the position*/
    def modifyRangeDependingOnAltitude(minRange: Double, maxRange: Double) = {
      val reductionOfTemperature = (elevationPlusPosition.elevation / 1000) * ConfigurationsApp.temperatureAltitudeVariation
      (minRange - reductionOfTemperature, maxRange - reductionOfTemperature)
    }

    val latitude = elevationPlusPosition.location.lat
    val (latitudeMinRange, latitudeMaxRange) = latitude.abs match {
      case absoluteLatitude if absoluteLatitude < ConfigurationsApp.equatorialLatitude =>
        (ConfigurationsApp.minEquatorialTemp, ConfigurationsApp.maxEquatorialTemp)
      case absoluteLatitude if absoluteLatitude < ConfigurationsApp.poleLatitude =>
        val initialMinRange = ConfigurationsApp.minMidTemp
        val initialMaxRange = ConfigurationsApp.maxMidTemp
        modifyRangeDependingOfSeason(initialMinRange, initialMaxRange, latitude)
      case _ =>
        val initialMinRange = ConfigurationsApp.minPoleTemp
        val initialMaxRange = ConfigurationsApp.maxPoleTemp
        modifyRangeDependingOfSeason(initialMinRange, initialMaxRange, latitude)
    }

    val (temporalMinRange, temporalMaxRange) = modifyRangeDependingHourOfDay(latitudeMinRange, latitudeMaxRange)
    val (finalMinRange, finalMaxRange) = modifyRangeDependingOnAltitude(temporalMinRange, temporalMaxRange)
    val randomValueTemperature = {
      maybeCurrentTemperature.fold(Randomiser.randomValueForLimits(finalMinRange, finalMaxRange)) {
        temperature =>
          val valueCurrentTemperature = temperature.degreesCelsius
          if(valueCurrentTemperature >= finalMinRange || valueCurrentTemperature <= finalMaxRange) {
            Randomiser.randomValueForLimits(valueCurrentTemperature - ConfigurationsApp.dailyVariation , valueCurrentTemperature + ConfigurationsApp.dailyVariation)
          } else {
            Randomiser.randomValueForLimits(finalMinRange, finalMaxRange)
          }
      }
    }
    Temperature(randomValueTemperature).truncateDoubles
  }
}
