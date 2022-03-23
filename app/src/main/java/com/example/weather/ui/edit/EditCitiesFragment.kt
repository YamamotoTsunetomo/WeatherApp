package com.example.weather.ui.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weather.databinding.FragmentEditCitiesBinding
import com.example.weather.network.WeatherApiServiceObject

class EditCitiesFragment : Fragment() {
    private lateinit var _binding: FragmentEditCitiesBinding
    val binding: FragmentEditCitiesBinding
    get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCitiesBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            if (binding.etField.text.isNotBlank()) {
                WeatherApiServiceObject.CITIES.add(binding.etField.text.toString())
                binding.etField.text.clear()
            } else {
                Toast.makeText(requireContext(), "Fill the Field!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRemove.setOnClickListener {
            if (binding.etField.text.isNotBlank()) {
                val city = binding.etField.text.toString()
                if (city !in WeatherApiServiceObject.CITIES)
                    Toast.makeText(requireContext(), "No Such City!", Toast.LENGTH_SHORT).show()
                else {
                    WeatherApiServiceObject.CITIES.remove(city)
                    binding.etField.text.clear()
                }
            } else {
                Toast.makeText(requireContext(), "Fill the Field!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}