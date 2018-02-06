package com.weather.models

import org.specs2.mutable.Specification


class ConditionsSpecs extends Specification {

  "ConditionsTrait" should {

    "return Snow when humidity is bigger than 50 and temperature less than 0" in {
      val temperature = Temperature(-1)
      val pressure = Pressure(3)
      val humidity = Humidity(51)
      Conditions.obtainCondition(temperature, pressure, humidity) === Conditions.Snow
    }

    "return Rain when humidity is bigger than 50 and temperature bigger than 0" in {
      val temperature = Temperature(1)
      val pressure = Pressure(3)
      val humidity = Humidity(51)
      Conditions.obtainCondition(temperature, pressure, humidity) === Conditions.Rain
    }

    "return Cloudy when humidity is less than 50 and pressure is bigger than 1020" in {
      val temperature = Temperature(-1)
      val pressure = Pressure(1021)
      val humidity = Humidity(40)
      Conditions.obtainCondition(temperature, pressure, humidity) === Conditions.Cloudy
    }

    "return Clear when humidity is less than 50 and pressure is less than 1020" in {
      val temperature = Temperature(1)
      val pressure = Pressure(1000)
      val humidity = Humidity(40)
      Conditions.obtainCondition(temperature, pressure, humidity) === Conditions.Clear
    }
  }

}
