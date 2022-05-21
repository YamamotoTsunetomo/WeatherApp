package com.example.weather.ui.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemWeatherBinding
import com.example.weather.model.WeatherUIModel
import com.example.weather.util.ImageLoader

class WeatherAdapter(
    private val imageLoader: ImageLoader,
    private val context: Context,
    private val onClickListener: (WeatherUIModel) -> Unit
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private val weatherData = mutableListOf<WeatherUIModel>()
    private var lastPosition = -1

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding,
        private val imageLoader: ImageLoader,
        private val onClickListener: (WeatherUIModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            weatherData: WeatherUIModel,
        ) {
            itemView.setOnClickListener { onClickListener(weatherData) }
            val imageUrl = getUrl(weatherData.icon)
            imageLoader.loadImage(imageUrl, binding.ivIcon)
            binding.tvTitle.text = weatherData.locationName
            binding.tvDescription.text = weatherData.description
            binding.tvTemperature.text = weatherData.temperature
        }

        private fun getUrl(icon: String): String =
            "https://openweathermap.org/img/wn/${icon}@2x.png"

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherViewHolder(
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            imageLoader,
            onClickListener
        )

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bindData(weatherData[position])
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

    override fun getItemCount() = weatherData.count()

    fun setData(weatherData: MutableList<WeatherUIModel>) {
        this.weatherData.clear()
        this.weatherData.addAll(weatherData)
        notifyDataSetChanged()
    }

    fun addItem(weatherUIModel: WeatherUIModel) {
        weatherData.add(weatherUIModel)
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int) {
        weatherData.removeAt(position)
        notifyItemRemoved(position)
    }

}
