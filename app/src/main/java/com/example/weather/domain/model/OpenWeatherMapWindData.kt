package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapWindData(
    val speed: Double,
    @field:Json(name = "deg")
    val degree: Double
)