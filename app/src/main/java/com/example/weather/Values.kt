package com.example.weather

object Values {
    const val TOKEN = "<YOUR_TOKEN>"

    const val RETROFIT_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val CITIES: MutableList<String> = mutableListOf(
        "Tbilisi", "Moscow", "Gori",
        "Baku", "New York", "Kyiv", "Atlanta",
        "Detroit", "Kutaisi", "Paris", "London"
    )

}
