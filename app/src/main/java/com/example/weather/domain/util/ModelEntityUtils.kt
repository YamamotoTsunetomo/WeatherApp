package com.example.weather.domain.util

import com.example.weather.data.db.WeatherEntity
import com.example.weather.domain.model.WeatherUIModel

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
