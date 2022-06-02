package com.example.weather.data.db

import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weathers")
    suspend fun getWeathers(): List<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weathers WHERE locationName=:locationName")
    suspend fun getByLocationName(locationName: String): WeatherEntity?

    @Delete
    suspend fun removeCity(weatherEntity: WeatherEntity)
}