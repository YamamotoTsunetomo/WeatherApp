package com.example.weather.model


sealed class WeatherUI {
    data class WeatherUICategory(val category: String) : WeatherUI()
    data class WeatherUIModel(
        val locationName: String,
        val status: String,
        val description: String,
        val icon: String,
        val temperature: String,
    ) : WeatherUI()
}