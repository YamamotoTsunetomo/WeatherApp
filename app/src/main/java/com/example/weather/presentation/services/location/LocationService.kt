package com.example.weather.presentation.services.location

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import androidx.core.app.ActivityCompat

class LocationService : Service(), LocationListener {


    private var locationManager: LocationManager? = null

    private val isNetworkEnabled: Boolean
        get() = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false

    override fun onLocationChanged(location: Location) = Unit

    override fun onBind(intent: Intent?): IBinder? = null

    fun getLocation(context: Context): Location? {
        locationManager = context
            .getSystemService(LOCATION_SERVICE) as LocationManager
        return if (isNetworkEnabled) locationManager?.let {

            val hasPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission)
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
