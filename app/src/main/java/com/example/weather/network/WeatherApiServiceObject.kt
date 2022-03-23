package com.example.weather.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object WeatherApiServiceObject {
    const val TOKEN = "7109dbedce09ad5b6dd0556aa8dbbeaa"

    private const val RETROFIT_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(RETROFIT_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val weatherApiService: OpenWeatherMapService =
        retrofit.create(OpenWeatherMapService::class.java)


    val CITIES: MutableList<String> = mutableListOf(
        "Tbilisi", "Moscow", "Gori",
        "Baku", "New York", "Kyiv", "Atlanta",
        "Detroit", "Kutaisi", "Paris", "London"
    )

}
