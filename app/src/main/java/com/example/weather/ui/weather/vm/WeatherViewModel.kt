package com.example.weather.ui.weather.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.OpenWeatherMapResponseData
import com.example.weather.model.WeatherUI
import com.example.weather.network.WeatherApiServiceObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class WeatherViewModel : ViewModel() {

    val weathers: LiveData<MutableList<WeatherUI>>
        get() = _weathers

    private val _weathers = MutableLiveData<MutableList<WeatherUI>>(mutableListOf())

    fun hasBeenHandled(): Boolean {
        weathers.value?.let {
            return it.size == WeatherApiServiceObject.CITIES.size
        }
        return false
    }

    private val kelvinToCelsius = fun(d: Double) = (d - 273.15).roundToInt().toString() + "\u2103"

    private fun handleResponse(
        response: Response<OpenWeatherMapResponseData>,
        weatherUiList: MutableList<WeatherUI>,
    ) {
        if (response.isSuccessful) {
            response.body()?.let {
                handleValidResponse(it, weatherUiList)
            } ?: Unit
        } else {
            Log.d("OpenWeatherMapResponse", "No data!")
        }
    }

    private fun handleValidResponse(
        response: OpenWeatherMapResponseData,
        weatherUiList: MutableList<WeatherUI>,
    ) {
        val weather = response.weather.firstOrNull()
        weather?.let {

            val locationName = response.locationName
            val status = weather.status
            val description = weather.description
            val icon = weather.icon
            val temperature = kelvinToCelsius(response.temperaturesData.temperature)
            weatherUiList.add(
                WeatherUI.WeatherUIModel(
                    locationName,
                    status,
                    description,
                    icon,
                    temperature,
                )
            )
        }

        this._weathers.value = weatherUiList
    }

    fun setWeathers(
        cities: List<String>,
        weatherUiList: MutableList<WeatherUI> = mutableListOf()
    ) {

        cities.forEach { city ->

            WeatherApiServiceObject.weatherApiService
                .getWeather(city, WeatherApiServiceObject.TOKEN)
                .enqueue(object : Callback<OpenWeatherMapResponseData> {
                    override fun onResponse(
                        call: Call<OpenWeatherMapResponseData>,
                        response: Response<OpenWeatherMapResponseData>
                    ) = handleResponse(response, weatherUiList)

                    override fun onFailure(
                        call: Call<OpenWeatherMapResponseData>,
                        t: Throwable
                    ) {
                        Log.d("OpenWeatherMapResponse", t.toString())
                    }
                })
        }

    }

}
