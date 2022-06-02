package com.example.weather.domain.util

data class Event<T>(private val value: T) {
    private var _isHandled: Boolean = false

    fun getValue(): T? =
        if (!_isHandled) {
            _isHandled = true
            value
        } else null
}
