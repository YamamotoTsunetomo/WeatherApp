package com.example.weather.presentation.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder

class LocationService : Service(), LocationListener {


    private var locationManager: LocationManager? = null

    private val isNetworkEnabled: Boolean
        get() = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

    override fun onLocationChanged(location: Location) = Unit

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    fun getLocation(context: Context): Location? {
        locationManager = context
            .getSystemService(LOCATION_SERVICE) as LocationManager
        return if (isNetworkEnabled) locationManager?.let {

            it.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_UPDATE_TIME,
                MIN_UPDATE_DISTANCE,
                this
            )

            it.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } else null
    }

    companion object {
        private const val MIN_UPDATE_TIME = 1000 * 60 * 10L
        private const val MIN_UPDATE_DISTANCE = 10_000f
    }
}
