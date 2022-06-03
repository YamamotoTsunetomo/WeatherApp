package com.example.weather.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathers")
data class WeatherEntity(
    @PrimaryKey
    val locationName: String,
    val status: String,
    val description: String,
    val icon: String,
    val temperature: String
)
