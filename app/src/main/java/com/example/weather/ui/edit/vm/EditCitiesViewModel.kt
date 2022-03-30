package com.example.weather.ui.edit.vm

import androidx.lifecycle.ViewModel
import com.example.weather.network.WeatherApiServiceObject


class EditCitiesViewModel : ViewModel() {

    val removeCity = fun(city: String) = WeatherApiServiceObject.CITIES.remove(city)

    val addCity = fun(city: String) = WeatherApiServiceObject.CITIES.add(city)

    val existsCity = fun(city: String) = city in WeatherApiServiceObject.CITIES

}
