package com.weather.models

import play.api.libs.json.{Json, Writes}


case class Location(location: String)


trait LocationImplicitTrait{

  implicit val locationWrites: Writes[Location] = Json.writes[Location]
}
