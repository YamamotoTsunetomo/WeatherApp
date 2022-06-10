package com.example.weather.domain.model

private const val DEFAULT_VALUE = ""

data class WeatherUIModel(
    // main
    val locationName: String = DEFAULT_VALUE,
    val status: String = DEFAULT_VALUE,
    val description: String = DEFAULT_VALUE,
    val icon: String = DEFAULT_VALUE,

    // temperature
    val temperature: String = DEFAULT_VALUE,
    val feelsLike: String = DEFAULT_VALUE,
    val minTemperature: String = DEFAULT_VALUE,
    val maxTemperature: String = DEFAULT_VALUE,
    val pressure: String = DEFAULT_VALUE,
    val humidity: String = DEFAULT_VALUE,

    // wind
    val windSpeed: String = DEFAULT_VALUE,
    val windDegree: String = DEFAULT_VALUE,

)
