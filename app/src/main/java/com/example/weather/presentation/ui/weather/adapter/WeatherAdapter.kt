package com.example.weather.presentation.ui.weather.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemWeatherBinding
import com.example.weather.domain.model.WeatherUIModel
import com.example.weather.domain.util.ImageLoader

class WeatherAdapter(
    private val imageLoader: ImageLoader,
    private val context: Context,
) : ListAdapter<WeatherUIModel, WeatherAdapter.WeatherViewHolder>(DiffCallback()) {
    private val weatherData = mutableListOf<WeatherUIModel>()
    private var lastPosition = -1
    private var isSetBlocked = false

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding,
        private val imageLoader: ImageLoader,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var isVisible = false

        private fun toggleVisibilityAndAnimate(view: View) {
            view.isVisible = isVisible
            val alpha = if (isVisible) 1f else 0f
            view.animate().alpha(alpha)
        }


        fun bindData(
            weatherData: WeatherUIModel,
        ) {
            itemView.setOnClickListener {
//                onClickListener(weatherData)

                with(binding) {
                    toggleVisibilityAndAnimate(tvMinTemperature)
                    toggleVisibilityAndAnimate(tvMaxTemperature)
                    toggleVisibilityAndAnimate(tvPressure)
                    toggleVisibilityAndAnimate(tvHumidity)
                    toggleVisibilityAndAnimate(tvWindSpeed)
                    toggleVisibilityAndAnimate(tvWindDegree)

                    toggleVisibilityAndAnimate(drawableSun)
                    toggleVisibilityAndAnimate(drawableMoon)
                    toggleVisibilityAndAnimate(drawableBarometer)
                    toggleVisibilityAndAnimate(drawableHumidity)
                    toggleVisibilityAndAnimate(drawableWind)
                    toggleVisibilityAndAnimate(drawableDegree)

                    isVisible = !isVisible
                }
            }

            // main
            val imageUrl = weatherData.icon
            val locationName = weatherData.locationName
            val description = weatherData.description

            // temperature
            val temperature = weatherData.temperature
            val feelsLike = weatherData.feelsLike
            val minTemperature = weatherData.minTemperature
            val maxTemperature = weatherData.maxTemperature
            val pressure = weatherData.pressure
            val humidity = weatherData.humidity

            // wind
            val windSpeed = weatherData.windSpeed
            val windDegree = weatherData.windDegree

            with(binding) {
                imageLoader.loadImage(imageUrl, ivIcon)
                tvTitle.text = locationName
                tvDescription.text = description
                tvTemperature.text = temperature
                tvFeelsLikeTemperature.text = feelsLike
                tvMinTemperature.text = minTemperature
                tvMaxTemperature.text = maxTemperature
                tvPressure.text = pressure
                tvHumidity.text = humidity
                tvWindSpeed.text = windSpeed
                tvWindDegree.text = windDegree
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WeatherViewHolder(
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            imageLoader,
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
        if (!isSetBlocked) {
            this.weatherData.clear()
            this.weatherData.addAll(weatherData)
            notifyDataSetChanged()
        }
        isSetBlocked = false
    }

    fun addItem(weatherUIModel: WeatherUIModel) {
        isSetBlocked = true
        weatherData.add(weatherUIModel)
        notifyItemInserted(itemCount)
    }

    fun removeItem(position: Int) {
        isSetBlocked = true
        weatherData.removeAt(position)
        notifyItemRemoved(position)
    }

    class DiffCallback : DiffUtil.ItemCallback<WeatherUIModel>() {
        override fun areItemsTheSame(oldItem: WeatherUIModel, newItem: WeatherUIModel): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: WeatherUIModel, newItem: WeatherUIModel): Boolean =
            oldItem == newItem
    }
}
