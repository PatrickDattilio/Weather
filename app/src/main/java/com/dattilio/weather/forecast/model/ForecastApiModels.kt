package com.dattilio.weather.forecast.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherApiModel(
    val list: List<HourlyWeatherResponse>,
    val city: City
)

data class City(
    val name: String,
    val coord: Coordinate
)

data class HourlyWeatherResponse(
    val dt: Long,
    val main: Temperature,
    val weather: List<WeatherStatus>,
    val wind: Wind,
    val clouds: Clouds?,
    val rain: Rain?,
    val snow: Snow?
)

data class Coordinate(val lat: Double, val lon: Double)

data class WeatherStatus(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Temperature(
    val temp_min: Double,
    val temp_max: Double
)

data class Wind(val speed: Double, val deg: Double)
data class Clouds(val all: String)
data class Rain(
    @Json(name = "1h") val oneHour: Int,
    @Json(name = "3h") val threeHour: Int
)

data class Snow(
    @Json(name = "1h") val oneHour: Int,
    @Json(name = "3h") val threeHour: Int
)

