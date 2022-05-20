package com.example.weather.app

import android.app.Application
import com.example.weather.di.tokenModule
import com.example.weather.di.viewModelModule
import com.example.weather.di.weatherApiServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                weatherApiServiceModule,
                viewModelModule,
                tokenModule
            )
        }
    }

}
