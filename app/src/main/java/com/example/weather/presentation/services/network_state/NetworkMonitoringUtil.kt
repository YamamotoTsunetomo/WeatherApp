package com.example.weather.presentation.services.network_state

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkMonitoringUtil(
    context: Context
) : ConnectivityManager.NetworkCallback() {

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        NetworkStateManager.setNetworkStatus(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        NetworkStateManager.setNetworkStatus(false)
    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun registerNetworkCallbackEvents() =
        connectivityManager.registerNetworkCallback(networkRequest, this)

}
