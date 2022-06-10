package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapTemperaturesData(
    @field:Json(name = "temp")
    val temperature: Double,
    @field:Json(name = "feels_like")
    val feelsLike: Double,
    @field:Json(name = "temp_min")
    val tempMin: Double,
    @field:Json(name = "temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)