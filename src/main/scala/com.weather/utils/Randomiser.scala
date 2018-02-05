package com.weather.utils

import scala.util.Random

object Randomiser extends RandomiserTrait

trait RandomiserTrait {

  def randomValueForLimits(minValue: Double, maxValue: Double) = {
    (Random.nextDouble() * (maxValue - minValue)) + minValue
  }

}
