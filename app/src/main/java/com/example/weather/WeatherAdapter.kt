package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.model.WeatherUIModel

class WeatherAdapter(
    private val layoutInflater: LayoutInflater,
    private val imageLoader: ImageLoader,
) : RecyclerView.Adapter<WeatherViewHolder>() {
    private val weatherData = mutableListOf<WeatherUIModel>()

    fun setData(weatherData: LiveData<MutableList<WeatherUIModel>>, owner: LifecycleOwner) {
        this.weatherData.clear()
        weatherData.observe(owner) {
            this.weatherData.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = layoutInflater.inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view, imageLoader)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindData(weatherData[position])
    }

    override fun getItemCount() = weatherData.count()

}