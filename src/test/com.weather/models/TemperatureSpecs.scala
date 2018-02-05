package com.weather.models

import org.joda.time.DateTime
import org.specs2.mutable.Specification

class TemperatureSpecs extends Specification {

  "Temperature" should {
    "return a value in summer day in the pole between -10°C and 15°C" in {
      val position = Position(-90, 0)
      val elevationPlusPosition = ElevationPlusPosition(0, position)
      val summerDayAntarctica = DateTime.parse("2018-12-01T12:00+00:00")
      Temperature.obtainRandomTemperature(elevationPlusPosition, summerDayAntarctica).degreesCelsius must beBetween(-10D, 15D)
    }

    "return a value in summer night in the pole between -15°C and 10°C" in {
      val position = Position(-90, 0)
      val elevationPlusPosition = ElevationPlusPosition(0, position)
      val summerNightAntarctica = DateTime.parse("2018-12-01T01:00+00:00")
      Temperature.obtainRandomTemperature(elevationPlusPosition, summerNightAntarctica).degreesCelsius must beBetween(-15D, 10D)
    }

    "return a value in winter day in the pole between -30°C and -5°C" in {
      val position = Position(-90, 0)
      val elevationPlusPosition = ElevationPlusPosition(0, position)
      val summerDayAntarctica = DateTime.parse("2018-06-01T12:00+00:00")
      Temperature.obtainRandomTemperature(elevationPlusPosition, summerDayAntarctica).degreesCelsius must beBetween(-30D, -5D)
    }

    "return a value in winter night in the pole between -35°C and -10°C" in {
      val position = Position(-90, 0)
      val elevationPlusPosition = ElevationPlusPosition(0, position)
      val summerNightAntarctica = DateTime.parse("2018-06-01T01:00+00:00")
      Temperature.obtainRandomTemperature(elevationPlusPosition, summerNightAntarctica).degreesCelsius must beBetween(-35D, -10D)
    }


  }

}
