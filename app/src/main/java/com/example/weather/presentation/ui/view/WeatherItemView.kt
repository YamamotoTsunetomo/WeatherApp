package com.example.weather.presentation.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.weather.databinding.ItemWeatherBinding
import com.example.weather.domain.util.ImageLoader

class WeatherItemView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding = ItemWeatherBinding
        .inflate(LayoutInflater.from(context), this, true)

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setDescription(description: String) {
        binding.tvDescription.text = description
    }

    fun setTemperature(temperature: String) {
        binding.tvTemperature.text = temperature
    }

    fun setImage(imageLoader: ImageLoader, url: String) {
        imageLoader.loadImage(url, binding.ivIcon)
    }

    fun setMinTemperature(t: String) {
        binding.tvMinTemperature.text = t
    }

    fun setMaxTemperature(t: String) {
        binding.tvMaxTemperature.text = t
    }

    fun setPressure(t: String) {
        binding.tvPressure.text = t
    }

    fun setHumidity(t: String) {
        binding.tvHumidity.text = t
    }

    fun setWindSpeed(t: String) {
        binding.tvWindSpeed.text = t
    }

    fun setWindDegree(t: String) {
        binding.tvWindDegree.text = t
    }

    fun showAdditionalComponents() = with(binding) {
        tvMinTemperature.isVisible = true
        tvMaxTemperature.isVisible = true
        tvPressure.isVisible = true
        tvHumidity.isVisible = true
        tvWindSpeed.isVisible = true
        tvWindDegree.isVisible = true

        drawableSun.isVisible = true
        drawableMoon.isVisible = true
        drawableHumidity.isVisible = true
        drawableBarometer.isVisible = true
        drawableDegree.isVisible = true
        drawableWind.isVisible = true
    }
}