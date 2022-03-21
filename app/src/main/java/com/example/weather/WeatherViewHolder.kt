package com.example.weather

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.model.WeatherUIModel

class WeatherViewHolder(
    containerView: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(containerView) {
    private val locationName: TextView = containerView.findViewById(R.id.tvTitle)
    private val status: TextView = containerView.findViewById(R.id.tvStatus)
    private val description: TextView = containerView.findViewById(R.id.tvDescription)
    private val icon: ImageView = containerView.findViewById<ImageView>(R.id.ivIcon)

    fun bindData(
        weatherData: WeatherUIModel,
    ) {
        val imageUrl = getUrl(weatherData.icon)
        imageLoader.loadImage(imageUrl, icon)
        locationName.text = weatherData.locationName
        status.text = weatherData.status
        description.text = weatherData.description
    }

    private fun getUrl(icon: String): String = "https://openweathermap.org/img/wn/${icon}@2x.png"
}