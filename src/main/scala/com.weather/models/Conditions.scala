package com.weather.models

import com.weather.models.Conditions._

object Conditions extends ConditionsTrait{
  sealed trait Condition

  case object Clear extends Condition
  case object Cloudy extends Condition
  case object Rain extends Condition
  case object Snow extends Condition
}


trait ConditionsTrait {

  def obtainCondition(temperature: Temperature, pressure: Pressure, humidity: Humidity): Condition = {
    (temperature.degreesCelsius, pressure.millibars, humidity.relativeHumidity) match {
      case (tempValue, _, humValue) if humValue > 50 && tempValue < 0 => Snow
      case (tempValue, _, humValue) if humValue > 50 && tempValue >= 0 => Rain
      case (_, pressValue, _) if pressValue > 1020 => Cloudy
      case _ => Clear
    }
  }

}
