package com.example.weather.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weathers")
    fun getWeathers(): Maybe<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCity(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weathers WHERE locationName=:locationName")
    fun getByLocationName(locationName: String): Maybe<WeatherEntity?>

    @Delete
    fun removeCity(weatherEntity: WeatherEntity)
}