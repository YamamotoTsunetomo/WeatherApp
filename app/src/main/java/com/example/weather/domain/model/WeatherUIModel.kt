package com.example.weather.domain.model


data class WeatherUIModel(
    val locationName: String,
    val status: String,
    val description: String,
    val icon: String,
    val temperature: String,
)
