package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapWindData(
    val windSpeed: Double,
    @field:Json(name = "deg")
    val windDegree: Double
)