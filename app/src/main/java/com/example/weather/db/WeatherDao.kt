package com.example.weather.db

import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weathers")
    suspend fun getWeathers(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(weatherEntity: WeatherEntity)

    @Delete
    suspend fun removeCity(weatherEntity: WeatherEntity)
}