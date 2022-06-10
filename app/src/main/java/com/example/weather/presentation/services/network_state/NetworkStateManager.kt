package com.example.weather.presentation.services.network_state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetworkStateManager {

    private val _isNetworkActive = MutableLiveData(false)

    val isNetworkActive: LiveData<Boolean>
        get() = _isNetworkActive

    fun setNetworkStatus(isActive: Boolean) {
        _isNetworkActive.postValue(isActive)
    }

}
