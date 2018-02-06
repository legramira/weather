package com.weather.models

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class HumiditySpecs extends Specification with Mockito {

  "Humidity" should {

    "have maximum value of relative humidity of 100" in {
      Humidity(101) === Humidity(100)
    }

    "have minimum value of relative humidity of 0" in {
      Humidity(-1) === Humidity(0)
    }

  }


  val humidityTraitMock = mock[HumidityTrait]
  val newPressure = Pressure(anyDouble)
  val pressure = Some(Pressure(anyDouble))
  val humidity = Some(Humidity(anyInt))
  "HumidityTrait" should {

    "have random Humidity between range 0-100" in {

      Humidity.obtainRandomHumidity(newPressure, pressure, humidity).relativeHumidity must beBetween(0, 100)
    }

  }


}
