package com.example.weather.di

import com.example.weather.network.OpenWeatherMapService
import com.example.weather.ui.weather.vm.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val RETROFIT_BASE_URL = "https://api.openweathermap.org/data/2.5/"

val tokenModule = module {
    single { "7109dbedce09ad5b6dd0556aa8dbbeaa" }
}

val weatherApiServiceModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(RETROFIT_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenWeatherMapService::class.java)
    }

}

val viewModelModule = module {
    viewModelOf(::WeatherViewModel)
}
