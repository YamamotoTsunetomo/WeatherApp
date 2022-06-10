package com.example.weather.presentation.ui.weather.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.db.WeatherDao
import com.example.weather.data.db.WeatherEntity
import com.example.weather.data.network.OpenWeatherMapService
import com.example.weather.domain.model.OpenWeatherMapResponseData
import com.example.weather.domain.model.WeatherUIModel
import com.example.weather.domain.util.Event
import com.example.weather.domain.util.ModelEntityUtils
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
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

    fun fetchData(isNetworkActive: Boolean) = viewModelScope.launch {
        if (!hasLoaded.value!!) {
            if (isNetworkActive)
                setWeathersFromApiAndStoreToDatabase()
            else setWeathersFromDatabase()
            delay(700)
            _hasLoaded.value = true
        }

    }

    // LiveData
    private val weatherList = mutableListOf<WeatherUIModel>()
    private val _weathers = MutableLiveData(weatherList)
    val weathers: LiveData<MutableList<WeatherUIModel>> get() = _weathers

    private val _hasLoaded = MutableLiveData(false)
    val hasLoaded: LiveData<Boolean>
        get() = _hasLoaded

    private val _addEvent = MutableLiveData(Event<WeatherUIModel?>(null))
    val addEvent: LiveData<Event<WeatherUIModel?>>
        get() = _addEvent

    private val _removeEvent = MutableLiveData(Event(-1))
    val removeEvent: LiveData<Event<Int>>
        get() = _removeEvent

    private val _currentLocationWeather = MutableLiveData<WeatherUIModel?>(null)
    val currentLocationWeather: LiveData<WeatherUIModel?>
        get() = _currentLocationWeather

    // Set
    private suspend fun setWeathersFromApiAndStoreToDatabase() {
        getWeathersFromDatabaseAsync().await()
            .forEach { city ->
                getWeatherFromApi(city.locationName) { addWeather(it) }
            }
    }

    private suspend fun setWeathersFromDatabase() {
        weatherList.clear()
        weatherList.addAll(
            getWeathersFromDatabaseAsync().await()
                .map { ModelEntityUtils.fromEntityToModel(it) }
                .toMutableList()
        )

        _weathers.value = weatherList
    }

    // Add
    private fun addWeatherToDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.addCity(weatherEntity) }

    private fun addWeather(weatherUIModel: WeatherUIModel) {
        addWeatherToDatabase(ModelEntityUtils.fromModelToEntity(weatherUIModel))
        weatherList.add(weatherUIModel)
        _weathers.value = weatherList
    }

    fun addWeather(city: String) {
        getWeatherFromApi(city) {
            _addEvent.value = Event(it)
            addWeather(it)
        }
    }

    fun addCurrentLocationWeather(city: String) =
        getWeatherFromApi(city) {
            _currentLocationWeather.value = it
        }

    // Remove
    private fun removeWeatherFromWeatherList(weatherUIModel: WeatherUIModel) {
        weatherList.remove(weatherUIModel)
        _weathers.value = weatherList
    }

    private fun removeWeatherFromDatabase(weatherEntity: WeatherEntity) =
        viewModelScope.launch { weatherDao.removeCity(weatherEntity) }

    fun removeWeather(position: Int) {
        _removeEvent.value = Event(position)
        val model = weatherList[position]
        removeWeatherFromDatabase(ModelEntityUtils.fromModelToEntity(model))
        removeWeatherFromWeatherList(model)
    }

    // Get
    private fun getWeathersFromDatabaseAsync() = viewModelScope.async {
        weatherDao.getWeathers()
    }

    private inline fun getWeatherFromApi(
        city: String,
        crossinline action: (WeatherUIModel) -> Unit
    ) {
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
                                    getUrl(w.icon),
                                    kelvinToCelsius(resp.temperaturesData.temperature),
                                )
                                action(model)
                            }
                        }
                }
            })
    }

    // Aux
    private val kelvinToCelsius = { d: Double -> (d - 273.15).roundToInt().toString() + "\u2103" }
    private val getUrl = { icon: String -> "https://openweathermap.org/img/wn/${icon}@2x.png" }
}
