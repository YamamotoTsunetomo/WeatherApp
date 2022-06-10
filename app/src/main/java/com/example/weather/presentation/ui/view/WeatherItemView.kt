package com.example.weather.presentation.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
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
}