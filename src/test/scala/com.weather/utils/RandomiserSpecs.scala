package com.weather.utils

import org.specs2.mutable.Specification


class RandomiserSpecs extends Specification {
  "Randomiser" should {
    "return a value between ranges" in {
      Randomiser.randomValueForLimits(0, 1) must beBetween(0D, 1D)
    }

    "not return a value less than the minimun" in {
      Randomiser.randomValueForLimits(0, 1) must not beLessThan 0
    }

    "not return a value bigger than the maximun" in {
      Randomiser.randomValueForLimits(0, 1) must not beGreaterThan 1
    }

    "return a value bigger than the minimun" in {
      Randomiser.randomValueForLimits(0, 1) must beGreaterThanOrEqualTo(0D)
    }

    "return a value less than the maximun" in {
      Randomiser.randomValueForLimits(0, 1) must beLessThanOrEqualTo(1D)
    }
  }
}