package com.weather.utils

import com.typesafe.config.ConfigFactory

object ConfigurationsApp {

  val configApp = ConfigFactory.load()
  val googleAPIKey = configApp.getString("google.api.key")
  val googleElevationUrl = configApp.getString("google.api.elevation.url")
  val defaultUndefinedLocation = configApp.getString("location.undefined")
  //  pressure configurations
  val minLandPressure = configApp.getDouble("earth.pressure.min")
  val maxLandPressure = configApp.getDouble("earth.pressure.max")
  val variationPressurePerAltitude = configApp.getDouble("earth.pressure.variations.altitude")
  val variationAveragePressure = configApp.getDouble("earth.pressure.variations.average")
  val highestLandAltitude = configApp.getDouble("earth.highestLand")
  //  temperature configurations
  val minEquatorialTemp = configApp.getDouble("earth.temperature.equatorialRange.min")
  val maxEquatorialTemp = configApp.getDouble("earth.temperature.equatorialRange.max")
  val minMidTemp = configApp.getDouble("earth.temperature.midRange.min")
  val maxMidTemp = configApp.getDouble("earth.temperature.midRange.max")
  val minPoleTemp = configApp.getDouble("earth.temperature.poleRange.min")
  val maxPoleTemp = configApp.getDouble("earth.temperature.poleRange.max")
  val seasonalVariation = configApp.getDouble("earth.temperature.variations.seasonal")
  val dailyVariation = configApp.getDouble("earth.temperature.variations.daily")
  val temperatureAltitudeVariation = configApp.getDouble("earth.temperature.variations.altitude")
  //  humidity configurations
  val humidityVariation = configApp.getInt("earth.humidity.variation.average")
  //  coordinates configurations
  val minLatitude = configApp.getDouble("earth.coordinates.latitude.min")
  val maxLatitude = configApp.getDouble("earth.coordinates.latitude.max")
  val minLongitude = configApp.getDouble("earth.coordinates.longitude.min")
  val maxLongitude = configApp.getDouble("earth.coordinates.longitude.max")
  val equatorialLatitude = configApp.getDouble("earth.coordinates.latitude.equatorial")
  val poleLatitude = configApp.getDouble("earth.coordinates.latitude.pole")


}
