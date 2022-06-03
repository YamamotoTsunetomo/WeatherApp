package com.example.weather.di

import androidx.room.Room
import com.example.weather.data.db.WeatherDatabase
import com.example.weather.data.network.OpenWeatherMapService
import com.example.weather.presentation.ui.weather.vm.WeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
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
    singleOf(::WeatherViewModel)
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            WeatherDatabase::class.java,
            "db"
        ).build()
    }
}

val weatherDaoModule = module {
    single { get<WeatherDatabase>().weatherDao }
}
