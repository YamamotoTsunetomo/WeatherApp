package com.example.weather.domain.model

import com.squareup.moshi.Json

data class OpenWeatherMapSysData(
    @field:Json(name = "sunrise")
    val sunRise: Long,
    @field:Json(name = "sunset")
    val sunSet: Long
)