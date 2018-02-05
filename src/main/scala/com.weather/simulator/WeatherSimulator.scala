package com.weather.simulator

import com.weather.models._
import org.joda.time.DateTime


trait WeatherSimulator {

  def simulateWeatherData(
                           location: Location,
                           elevationPlusPosition: ElevationPlusPosition,
                           dateTime: DateTime,
                           maybeCurrentTemperature: Option[Temperature] = None,
                           maybeCurrentPressure: Option[Pressure] = None,
                           maybeCurrentHumidity: Option[Humidity] = None
                         ): WeatherData = {
    val newTemperature = Temperature.obtainRandomTemperature(elevationPlusPosition, dateTime, maybeCurrentTemperature)
    val newPressure = Pressure.obtainRandomPressure(elevationPlusPosition, newTemperature, maybeCurrentTemperature, maybeCurrentPressure)
    val newHumidity = Humidity.obtainRandomHumidity(newPressure, maybeCurrentPressure, maybeCurrentHumidity)
    val newCondition = Conditions.obtainCondition(newTemperature, newPressure, newHumidity)

    WeatherData(
      location = location,
      elevationPlusPosition = elevationPlusPosition,
      dateTime = dateTime,
      condition = newCondition,
      temperature = newTemperature,
      pressure = newPressure,
      humidity = newHumidity
    )
  }

}
