package com.example.weather.ui.weather.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.db.WeatherDao
import com.example.weather.db.WeatherEntity
import com.example.weather.model.OpenWeatherMapResponseData
import com.example.weather.model.WeatherUIModel
import com.example.weather.network.OpenWeatherMapService
import com.example.weather.util.ModelEntityUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class WeatherViewModel(
    private val weatherApiService: OpenWeatherMapService,
    private val weatherDao: WeatherDao,
    private val token: String
) : ViewModel() {

    val weathers: LiveData<MutableList<WeatherUIModel>>
        get() = _weathers

    var hasItemBeenRemoved = false

    private var _hasBeenHandled = false

    val hasBeenHandled
        get() = _hasBeenHandled

    val handle: () -> Unit = { _hasBeenHandled = true }

    private val weatherList = mutableListOf<WeatherUIModel>()
    private val _weathers = MutableLiveData(weatherList)

    private val kelvinToCelsius = { d: Double -> (d - 273.15).roundToInt().toString() + "\u2103" }

    suspend fun setWeathersFromApiToDatabase() =
        getWeathersFromDatabase().await()
            .forEach { city -> fetchWeatherAndAddToDatabase(city.locationName) }


    fun getWeathersFromDatabase() = viewModelScope.async {
        weatherDao.getWeathers()
    }


    fun removeWeather(position: Int) {
        hasItemBeenRemoved = true
        val model = weatherList[position]
        removeWeatherFromDatabase(ModelEntityUtils.fromModelToEntity(model))
        removeWeatherFromWeatherList(model)
    }

    private fun removeWeatherFromWeatherList(weatherUIModel: WeatherUIModel) {
        weatherList.remove(weatherUIModel)
        _weathers.value = weatherList
    }

    private fun removeWeatherFromDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.removeCity(weatherEntity) }

    private fun addWeatherToDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.addCity(weatherEntity) }

    fun addWeatherToWeatherList(weatherUIModel: WeatherUIModel) =
        weatherList.add(weatherUIModel)

    fun fetchWeatherAndAddToDatabase(city: String) {
        weatherList.clear()
        weatherApiService.fetchWeather(city, token)

            .enqueue(object : Callback<OpenWeatherMapResponseData> {

                override fun onFailure(c: Call<OpenWeatherMapResponseData>, t: Throwable) = Unit

                override fun onResponse(
                    call: Call<OpenWeatherMapResponseData>,
                    response: Response<OpenWeatherMapResponseData>
                ) {
                    if (response.isSuccessful)
                        response.body()?.let { resp ->
                            val weather = resp.weather.firstOrNull()
                            weather?.let { w ->
                                val model = WeatherUIModel(
                                    resp.locationName,
                                    w.status,
                                    w.description,
                                    w.icon,
                                    kelvinToCelsius(resp.temperaturesData.temperature),
                                )

                                addWeatherToWeatherList(model)
                                addWeatherToDatabase(ModelEntityUtils.fromModelToEntity(model))
                                _weathers.value = weatherList
                            }
                        }
                }
            })

    }


}
