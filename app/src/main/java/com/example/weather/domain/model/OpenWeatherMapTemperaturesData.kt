package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapTemperaturesData(
    @field:Json(name = "temp")
    val temperature: Double,
    @field:Json(name = "feels_like")
    val feelsLike: Double,
    @field:Json(name = "temp_min")
    val minTemperature: Double,
    @field:Json(name = "temp_max")
    val maxTemperature: Double,
    val pressure: Int,
    val humidity: Int
)