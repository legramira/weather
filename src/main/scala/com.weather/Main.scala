package com.weather

import com.typesafe.scalalogging.LazyLogging
import com.weather.gateways.GoogleElevationAPI
import com.weather.models.Position
import com.weather.simulator.WeatherSimulator
import com.weather.utils.{ConfigurationsApp, Randomiser}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.util.Random


object Main
  extends App
    with LazyLogging
    with WeatherSimulator {

  /** Get some random initial positions Software can be modified to be introduce by the user */
  val initialRandomPositions = {
    for {
      _ <- 1 to 10
    } yield {
      val position = Position(
        Randomiser.randomValueForLimits(ConfigurationsApp.minLatitude, ConfigurationsApp.maxLatitude),
        Randomiser.randomValueForLimits(ConfigurationsApp.minLongitude, ConfigurationsApp.maxLongitude)
      )
      logger.debug(s"new Position generated as: $position")
      position
    }
  }
  /** From initial positions get Elevation and location, this two are calls to API, reason why are
    * asynchronous values, then all of this values are going to be need it to calculate the other data
    * dateTime is random Future time in the next day */
  val eventualSequenceOfInitialWeatherData = {
    initialRandomPositions.map { position =>
      val randomInitialDateTime = DateTime.now().plusHours(Random.nextInt(24))
      for {
        location <- position.truncateDoubles.toEventualLocation
        elevationPlusPosition <- GoogleElevationAPI.getElevationForPosition(position)
      } yield {
        logger.debug(s"new Location generated as: $location")
        logger.debug(s"new elevationPlusPosition generated as: $elevationPlusPosition")
        simulateWeatherData(location, elevationPlusPosition.truncateDoubles, randomInitialDateTime)
      }
    }
  }
  /** Now generate data for next 10 hours for each of the initial Weather data points */
  val eventualResultSimulation = {
    Future.sequence(eventualSequenceOfInitialWeatherData).map { initialSequenceWeatherData =>
      initialSequenceWeatherData.flatMap { initialWeatherData =>
        for {
          hours <- 1 to 10
        } yield {
          val nextDateTime = initialWeatherData.dateTime.plusHours(hours)
          simulateWeatherData(
            initialWeatherData.location,
            initialWeatherData.elevationPlusPosition,
            nextDateTime,
            Some(initialWeatherData.temperature),
            Some(initialWeatherData.pressure),
            Some(initialWeatherData.humidity)
          )
        }
      }
    }
  }
  val now = System.currentTimeMillis()
  logger.debug(s"all futures set to run, waiting for the results")
  /** wait for the asynchronous results and print them */
  val resultSimulation = scala.concurrent.Await.result(eventualResultSimulation, Duration.Inf)
  logger.debug(s"all futures solved in ${System.currentTimeMillis() - now}msecs")
  resultSimulation.foreach(println)

}
