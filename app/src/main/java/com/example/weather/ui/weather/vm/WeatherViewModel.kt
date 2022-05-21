package com.example.weather.ui.weather.vm

import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class WeatherViewModel(
    private val weatherApiService: OpenWeatherMapService,
    private val weatherDao: WeatherDao,
    private val token: String
) : ViewModel() {

    // LiveData
    private val weatherList = mutableListOf<WeatherUIModel>()
    private val _weathers = MutableLiveData(weatherList)
    val weathers: LiveData<MutableList<WeatherUIModel>> get() = _weathers

    private val _currentAddedItem =
        MutableLiveData<Pair<Boolean, WeatherUIModel?>>(Pair(false, null))
    val currentAddedItem: LiveData<Pair<Boolean, WeatherUIModel?>> get() = _currentAddedItem
    val setCurrentItem: (Pair<Boolean, WeatherUIModel?>) -> Unit = { _currentAddedItem.value = it }


    // Handlers
    private var _hasBeenRemoved = false
    val hasBeenRemoved get() = _hasBeenRemoved
    val handleRemove: (Boolean) -> Unit = { _hasBeenRemoved = it }

    private var _hasBeenSet = false
    val hasBeenSet get() = _hasBeenSet
    val handleSet: (Boolean) -> Unit = { _hasBeenSet = it }

    private var _hasBeenAdded = false
    val hasBeenAdded get() = _hasBeenAdded
    val handleAdd: (Boolean) -> Unit = { _hasBeenAdded = it }

    // Set
    suspend fun setWeathersFromApiAndStoreToDatabase() =
        getWeathersFromDatabaseAsync().await()
            .forEach { city -> getWeatherFromApiAndAddToDatabase(city.locationName) }

    suspend fun setWeathersFromDatabase() {
        getWeathersFromDatabaseAsync().await()
            .forEach { city ->
                addWeatherToWeatherList(ModelEntityUtils.fromEntityToModel(city))
                _weathers.value = weatherList
            }
    }

    // Add
    private fun addWeatherToDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.addCity(weatherEntity) }

    private fun addWeatherToWeatherList(weatherUIModel: WeatherUIModel) {
        weatherList.add(weatherUIModel)
    }

    fun addWeather(weatherUIModel: WeatherUIModel) {
        Log.d("WWWWW", "adding ${weatherUIModel.locationName}")
        addWeatherToDatabase(ModelEntityUtils.fromModelToEntity(weatherUIModel))
        addWeatherToWeatherList(weatherUIModel)
        _weathers.value = weatherList
//        if (_hasBeenAdded)
        setCurrentItem(Pair(true, weatherUIModel))
    }

    // Remove
    private fun removeWeatherFromWeatherList(weatherUIModel: WeatherUIModel) {
        weatherList.remove(weatherUIModel)
        _weathers.value = weatherList
    }

    private fun removeWeatherFromDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.removeCity(weatherEntity) }

    fun removeWeather(position: Int) {
        handleRemove(true)
        val model = weatherList[position]
        removeWeatherFromDatabase(ModelEntityUtils.fromModelToEntity(model))
        removeWeatherFromWeatherList(model)
    }


    private fun getWeathersFromDatabaseAsync() = viewModelScope.async {
        weatherDao.getWeathers()
    }

    fun getWeatherFromApiAndAddToDatabase(city: String) {
        if (!_hasBeenAdded)
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
//                                viewModelScope.launch {
                                addWeather(model)
//                                }
                            }
                        }
                }
            })

    }

    // Aux
    private val kelvinToCelsius = { d: Double -> (d - 273.15).roundToInt().toString() + "\u2103" }

}
