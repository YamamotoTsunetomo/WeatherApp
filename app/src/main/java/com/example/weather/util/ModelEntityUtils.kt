package com.example.weather.util

import com.example.weather.db.WeatherEntity
import com.example.weather.model.WeatherUIModel

object ModelEntityUtils {
    val fromModelToEntity: (WeatherUIModel) -> WeatherEntity = {
        WeatherEntity(
            it.locationName, it.status, it.description,
            it.icon, it.temperature
        )
    }

    val fromEntityToModel: (WeatherEntity) -> WeatherUIModel = {
        WeatherUIModel(
            it.locationName, it.status, it.description,
            it.icon, it.temperature
        )
    }
}
