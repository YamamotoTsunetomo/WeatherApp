package com.example.weather.util

class Event<T>(
    val eventValue: T? = null
) {
    var hasBeenHandled = false

    fun handle() {
        hasBeenHandled = true
    }
}

