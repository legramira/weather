package com.weather.simulator

import com.weather.models._
import org.joda.time.DateTime


trait WeatherSimulator
  extends TemperatureTrait
    with PressureTrait
    with HumidityTrait
    with ConditionsTrait {

  def simulateWeatherData(
                           location: Location,
                           elevationPlusPosition: ElevationPlusPosition,
                           dateTime: DateTime,
                           maybeCurrentTemperature: Option[Temperature] = None,
                           maybeCurrentPressure: Option[Pressure] = None,
                           maybeCurrentHumidity: Option[Humidity] = None
                         ): WeatherData = {
    val newTemperature = obtainRandomTemperature(elevationPlusPosition, dateTime, maybeCurrentTemperature)
    val newPressure = obtainRandomPressure(elevationPlusPosition, newTemperature, maybeCurrentTemperature, maybeCurrentPressure)
    val newHumidity = obtainRandomHumidity(newPressure, maybeCurrentPressure, maybeCurrentHumidity)
    val newCondition = obtainCondition(newTemperature, newPressure, newHumidity)

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
