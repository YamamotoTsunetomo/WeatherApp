package com.example.weather.data.network

import com.example.weather.domain.model.OpenWeatherMapResponseData
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
