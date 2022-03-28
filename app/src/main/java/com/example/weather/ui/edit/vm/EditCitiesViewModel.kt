package com.example.weather.ui.edit.vm

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.network.WeatherApiServiceObject
import com.example.weather.ui.weather.WeatherFragment
import com.example.weather.ui.weather.vm.WeatherViewModel

private const val TOAST_MESSAGE = "Fill the field!"

class EditCitiesViewModel : ViewModel() {

    fun btnAddOnClickListener(cityField: EditText, context: Context) {
        if (hasFilled(cityField)) {
            addCity(cityField.text.toString())
            clearField(cityField)
        } else showToast(context)
    }

    fun btnRemoveOnClickListener(cityField: EditText, context: Context) {
        if (hasFilled(cityField)) {
            val city = cityField.text.toString()
            if (city !in WeatherApiServiceObject.CITIES)
                showToast(context, "No such city!")
            else {
                removeCity(cityField.text.toString())
                clearField(cityField)
            }
        } else showToast(context)
    }

    private fun removeCity(city: String) = WeatherApiServiceObject.CITIES.remove(city)

    private fun addCity(city: String) = WeatherApiServiceObject.CITIES.add(city)

    private fun hasFilled(et: EditText) = et.text.isNotBlank()

    private fun clearField(et: EditText) = et.text.clear()

    private fun showToast(context: Context, text: String = TOAST_MESSAGE) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}
