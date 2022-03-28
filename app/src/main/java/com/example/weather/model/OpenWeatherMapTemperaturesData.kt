package com.example.weather.model

import com.squareup.moshi.Json

data class OpenWeatherMapTemperaturesData(
    @field:Json(name = "temp")
    val temperature: Double,
)