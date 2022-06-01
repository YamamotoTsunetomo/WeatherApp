package com.example.weather.ui.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.databinding.FragmentAddCityBinding
import com.example.weather.ui.weather.vm.WeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*

class AddCityFragment : BottomSheetDialogFragment() {

    private val weatherViewModel: WeatherViewModel by inject()

    private var _binding: FragmentAddCityBinding? = null
    val binding: FragmentAddCityBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val city = binding.etAddTitle.text.toString()
            when {
                city.isEmpty() -> makeToast(requireContext(), "Fill the field")
                weatherViewModel.weathers.value!!.map { it.locationName.lowercase(Locale.getDefault()) }
                    .contains(
                        city.lowercase(Locale.getDefault())
                    ) -> {
                    makeToast(requireContext(), "City Already In The List!")
                }
                else -> {
                    weatherViewModel.handleAdd(true)
                    lifecycleScope.launch {
                        weatherViewModel.getWeatherFromApiAndAddToDatabase(city)
                        dismiss()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    private fun makeToast(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}