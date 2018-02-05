package com.weather.gateways

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import com.weather.models.{ElevationPlusPosition, ElevationResponseAPI, Position}
import play.api.libs.json.JsValue
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.StandaloneWSClient
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import com.weather.utils.ConfigurationsApp

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future


object GoogleElevationAPI extends GoogleElevationAPITrait with LazyLogging


trait GoogleElevationAPITrait {
  self: LazyLogging =>

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  def getElevationForPosition(position: Position): Future[ElevationPlusPosition] = {
    val wsClient = StandaloneAhcWSClient()
    val queryStringParameters = Seq(
      "locations" -> position.toString,
      "key" -> ConfigurationsApp.googleAPIKey
    )
    getDataFromCall(wsClient, queryStringParameters: _*)
      .map{ eitherElevation =>
        eitherElevation.fold(
          errorAPI => {
            logger.error(s"Error from external API: $errorAPI, setting position to default elevation")
            ElevationPlusPosition(0d, position)
          },
          elevationResponse => {
            val result = elevationResponse.results.headOption.getOrElse(ElevationPlusPosition(0d, position))
            logger.debug(s"Final elevation determined as ${result.elevation}")
            result
          }
        )
      }
      .andThen { case _ => wsClient.close() }
      .andThen { case _ => system.terminate() }
  }

  private def getDataFromCall(wsClient: StandaloneWSClient, queryStringParameters: (String, String)*): Future[Either[String, ElevationResponseAPI]] = {
    import com.weather.models.ElevationResponseAPIImplicits._
    wsClient
      .url(ConfigurationsApp.googleElevationUrl)
      .withQueryStringParameters(queryStringParameters: _*)
      .get()
      .map { response =>
        val statusCode = response.statusText
        if(statusCode == "OK") {
          val body = response.body[JsValue]
          body.validate[ElevationResponseAPI].fold(
            error => Left(error.toString),
            elevationResponse => Right(elevationResponse)
          )
        } else {
         Left(s"Issue getting connection with elevation API, status code: $statusCode")
        }
      }
  }


}