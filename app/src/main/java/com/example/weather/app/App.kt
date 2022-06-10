package com.example.weather.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.weather.R
import com.example.weather.di.*
import com.example.weather.presentation.services.network_state.NetworkMonitoringUtil
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
                tokenModule,
                databaseModule,
                weatherDaoModule
            )
        }
        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel("channelID", getString(R.string.app_name), importance).apply {
                    description = ""
                }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

            NetworkMonitoringUtil(this).registerNetworkCallbackEvents()
        }
    }

}
