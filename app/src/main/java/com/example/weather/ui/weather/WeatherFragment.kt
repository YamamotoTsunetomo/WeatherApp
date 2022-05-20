package com.example.weather.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.util.TemporaryConstants
import com.example.weather.ui.weather.adapter.WeatherAdapter
import com.example.weather.ui.weather.vm.WeatherViewModel
import com.example.weather.util.GlideImageLoader
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private var _binding: FragmentWeatherBinding? = null

    private val weatherViewModel: WeatherViewModel by viewModel()

    private val weatherAdapter by lazy {
        WeatherAdapter(
            layoutInflater,
            GlideImageLoader(requireContext()),
            requireContext()
        ) { weatherData ->
            val messageBody = weatherData.locationName
            AlertDialog.Builder(requireContext())
                .setTitle("TITLE")
                .setMessage(messageBody)
                .setPositiveButton("OK") { _, _ -> }
                .show()
        }
    }

    val binding: FragmentWeatherBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        recycler = binding.recyclerView

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = weatherAdapter

        if (!weatherViewModel.hasBeenHandled()) {
            weatherViewModel.setWeathers(TemporaryConstants.CITIES)
        }


        weatherViewModel.weathers.observe(viewLifecycleOwner) {
            weatherAdapter.setData(weatherViewModel.weathers.value!!)
        }

        binding.btnNavigateToEdit.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_go_to_edit)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
