package com.example.weather.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.room.Room
import com.example.weather.db.WeatherDatabase
import com.example.weather.network.OpenWeatherMapService
import com.example.weather.ui.weather.vm.WeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
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

val networkCheckerModule = module {
    fun isNetworkActive(app: Application): Boolean {
        val connectivityManager =
            (app.getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager
                .getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            if (connectivityManager.activeNetworkInfo != null &&
                connectivityManager.activeNetworkInfo!!
                    .isConnectedOrConnecting
            )
                return true
        }
        return false
    }

    single { isNetworkActive(androidApplication()) }
}
