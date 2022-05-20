package com.example.weather.network

import com.example.weather.model.OpenWeatherMapResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {
    @GET("weather")
    fun fetchWeather(
        @Query("q") location: String,
        @Query("appid") token: String,
    ): Call<OpenWeatherMapResponseData>
}
