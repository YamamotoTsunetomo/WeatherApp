package com.example.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.api.OpenWeatherMapService
import com.example.weather.model.OpenWeatherMapResponseData
import com.example.weather.model.WeatherUIModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class WeatherViewModel : ViewModel() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Values.RETROFIT_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    var isRecyclerVisible = false

    private val weatherApiService =
        retrofit.create(OpenWeatherMapService::class.java)

    private var _weathers =
        getWeathers(Values.CITIES)

    val weathers: LiveData<MutableList<WeatherUIModel>>
        get() = _weathers

    fun restartWeathers() {
        _weathers = getWeathers(Values.CITIES)
    }


    private fun handleResponse(
        response: Response<OpenWeatherMapResponseData>,
        weathers: MutableList<WeatherUIModel>
    ) {
        if (response.isSuccessful) {
            response.body()?.let {
                handleValidResponse(it, weathers)
            } ?: Unit
        } else {
            Log.d("OpenWeatherMapResponse", "No data!")
        }
    }

    private fun handleValidResponse(
        response: OpenWeatherMapResponseData,
        weathers: MutableList<WeatherUIModel>
    ) {
        val weather = response.weather.firstOrNull()
        weather?.let {
            val locationName = response.locationName
            val status = weather.status
            val description = weather.description
            val icon = weather.icon
            weathers.add(WeatherUIModel(locationName, status, description, icon))
            Log.d("OpenWeatherMapResponse", weathers.toString())
        }
    }

    private fun getWeathers(
        cities: List<String>,
    ): LiveData<MutableList<WeatherUIModel>> {
        val result = MutableLiveData<MutableList<WeatherUIModel>>(mutableListOf())
        cities.forEach { city ->
            weatherApiService
                .getWeather(city, Values.TOKEN)
                .enqueue(object : Callback<OpenWeatherMapResponseData> {
                    override fun onResponse(
                        call: Call<OpenWeatherMapResponseData>,
                        response: Response<OpenWeatherMapResponseData>
                    ) = handleResponse(response, result.value!!)

                    override fun onFailure(call: Call<OpenWeatherMapResponseData>, t: Throwable) {
                        Log.d("OpenWeatherMapResponse", t.toString())
                    }
                })
        }

        return result
    }

}
