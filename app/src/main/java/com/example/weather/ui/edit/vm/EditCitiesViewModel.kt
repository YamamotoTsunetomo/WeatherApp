package com.example.weather.ui.edit.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.util.TemporaryConstants
import com.example.weather.util.Event


class EditCitiesViewModel : ViewModel() {

    val showToast = MutableLiveData<Event<String>>()
    val clearCityField = MutableLiveData<Event<Unit>>()

    val removeCity = fun(city: String) = TemporaryConstants.CITIES.remove(city)

    val addCity = fun(city: String) = TemporaryConstants.CITIES.add(city)

    val existsCity = fun(city: String) = city in TemporaryConstants.CITIES

    fun onAddClicked(cityFieldText: String) {
        if (hasFilled(cityFieldText)) {
            addCity(cityFieldText)
            clearCityField.value = Event()
        } else {
            showToast.value = Event("Fill the Field")
        }
    }

    fun onRemoveClicked(cityFieldText: String) {
        if (hasFilled(cityFieldText)) {
            if (!existsCity(cityFieldText))
                showToast.value = Event("No such City")
            else {
                removeCity(cityFieldText)
                clearCityField.value = Event()
            }
        } else showToast.value = Event("Fill the Field")
    }

    private fun hasFilled(et: String) = et.isNotBlank()

}

