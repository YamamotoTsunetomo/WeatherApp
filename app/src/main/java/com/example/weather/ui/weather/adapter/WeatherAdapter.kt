package com.example.weather.ui.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.util.ImageLoader
import com.example.weather.R
import com.example.weather.model.*
import java.lang.IllegalStateException

class WeatherAdapter(
    private val layoutInflater: LayoutInflater,
    private val imageLoader: ImageLoader,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val weatherData = mutableListOf<WeatherUI>()
    private var lastPosition = -1

    inner class WeatherViewHolder(
        containerView: View,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(containerView) {
        private val locationName: TextView = containerView.findViewById(R.id.tvTitle)
        private val description: TextView = containerView.findViewById(R.id.tvDescription)
        private val icon: ImageView = containerView.findViewById(R.id.ivIcon)
        private val temperature: TextView = containerView.findViewById(R.id.tvTemperature)

        fun bindData(
            weatherData: WeatherUI.WeatherUIModel,
        ) {
            val imageUrl = getUrl(weatherData.icon)
            imageLoader.loadImage(imageUrl, icon)
            locationName.text = weatherData.locationName
            description.text = weatherData.description
            temperature.text = weatherData.temperature
        }

        private fun getUrl(icon: String): String =
            "https://openweathermap.org/img/wn/${icon}@2x.png"
    }

    inner class CategoryViewHolder(
        containerView: View
    ) : RecyclerView.ViewHolder(containerView) {
        private val category: TextView = containerView.findViewById(R.id.tvWeatherCategoryName)

        fun bindData(weatherCategory: WeatherUI.WeatherUICategory) {
            category.text = weatherCategory.category
        }
    }

    fun setData(weatherData: MutableList<WeatherUI>) {
        this.weatherData.clear()
        this.weatherData.addAll(weatherData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val view = layoutInflater.inflate(R.layout.item_weather, parent, false)
//        return WeatherViewHolder(view, imageLoader)
        return when (viewType) {
            R.layout.item_weather_category -> {
                CategoryViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_weather_category,
                        parent,
                        false
                    )
                )
            }
            R.layout.item_weather -> {
                WeatherViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_weather,
                        parent,
                        false
                    ), imageLoader
                )
            }
            else -> throw IllegalStateException("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        holder.bindData(weatherData[position])
        when (val weather = weatherData[position]) {
            is WeatherUI.WeatherUICategory -> (holder as CategoryViewHolder).bindData(weather)
            is WeatherUI.WeatherUIModel -> (holder as WeatherViewHolder).bindData(weather)
        }
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in)
            itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (weatherData[position]) {
            is WeatherUI.WeatherUICategory -> R.layout.item_weather_category
            is WeatherUI.WeatherUIModel -> R.layout.item_weather
        }
    }

    override fun getItemCount() = weatherData.count()

}

