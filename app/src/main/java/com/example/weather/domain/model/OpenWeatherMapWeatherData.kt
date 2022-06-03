package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapWeatherData(
    @field:Json(name = "main")
    val status: String,
    val description: String,
    val icon: String
)