package com.example.weather.ui.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.extensions.ImageLoader
import com.example.weather.R
import com.example.weather.model.WeatherUIModel

class WeatherAdapter(
    private val layoutInflater: LayoutInflater,
    private val imageLoader: ImageLoader,
    private val context: Context
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private val weatherData = mutableListOf<WeatherUIModel>()
    private var lastPosition = -1

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

    fun setData(weatherData: MutableList<WeatherUIModel>) {
        this.weatherData.clear()
        this.weatherData.addAll(weatherData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = layoutInflater.inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view, imageLoader)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindData(weatherData[position])

        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in)
            itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount() = weatherData.count()

}

