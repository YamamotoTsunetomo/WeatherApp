package com.example.weather.presentation.ui.weather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.domain.model.WeatherUIModel
import com.example.weather.domain.util.GlideImageLoader
import com.example.weather.presentation.services.network_state.NetworkStateManager
import com.example.weather.presentation.services.location.LocationService
import com.example.weather.presentation.ui.weather.adapter.WeatherAdapter
import com.example.weather.presentation.ui.weather.vm.WeatherViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class WeatherFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private var _binding: FragmentWeatherBinding? = null

    private val weatherViewModel: WeatherViewModel by viewModel()

    private val weatherAdapter by lazy {
        WeatherAdapter(
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

    private val locationService by lazy {
        LocationService()
    }

    private val geocoder by lazy {
        Geocoder(requireContext(), Locale.getDefault())
    }

    val binding: FragmentWeatherBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObservers()
        initRecycler()


        if (weatherViewModel.currentLocationWeather.value == null)
            getCurrentLocation()

        weatherViewModel.fetchData(NetworkStateManager.isNetworkActive.value!!)
        binding.btnNavigateToEdit.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_addCityFragment)
        }
    }

    private fun getCurrentLocation() {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                locationService.getLocation(requireContext())?.let { location ->
                    val currentLocation = getCityFromLocation(
                        location.latitude,
                        location.longitude
                    )
                    weatherViewModel.addCurrentLocationWeather(currentLocation)
                }
            }
        }.launch(ACCESS_COARSE_LOCATION)
    }

    private fun setCurrentLocationWeatherItem(weatherUIModel: WeatherUIModel) =
        with(binding) {
            wivCurrentLocation.setTitle(weatherUIModel.locationName)
            wivCurrentLocation.setDescription(weatherUIModel.description)
            wivCurrentLocation.setTemperature(weatherUIModel.temperature)
            wivCurrentLocation.setImage(GlideImageLoader(requireContext()), weatherUIModel.icon)
        }

    private fun initObservers() {

        NetworkStateManager.isNetworkActive.observe(viewLifecycleOwner) {
            val msg = if (it) "active" else "lost"
            makeToast(requireContext(), msg)
        }

        weatherViewModel.currentLocationWeather.observe(viewLifecycleOwner) {
            it?.let {
                setCurrentLocationWeatherItem(it)
            }
        }

        weatherViewModel.removeEvent.observe(viewLifecycleOwner) {
            it.getValue()?.let { pos ->
                if (pos > -1)
                    weatherAdapter.removeItem(pos)
            }
        }

        weatherViewModel.addEvent.observe(viewLifecycleOwner) {
            it.getValue()?.let { model ->
                weatherAdapter.addItem(model)
            }
        }

        weatherViewModel.hasLoaded.observe(viewLifecycleOwner) {
            if (it)
                stopLoading()
        }

        weatherViewModel.weathers.observe(viewLifecycleOwner) {
            weatherAdapter.setData(it)
        }
    }

    private fun initRecycler() {
        recycler = binding.recyclerView
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = weatherAdapter
        setOnSwipeDelete()
    }

    private fun stopLoading() {
        binding.tvLoading.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.btnNavigateToEdit.visibility = View.VISIBLE
        binding.wivCurrentLocation.visibility = View.VISIBLE
    }

    private fun setOnSwipeDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                lifecycleScope.launch {
                    weatherViewModel.removeWeather(position)
                }
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun getCityFromLocation(lat: Double, lon: Double) =
        geocoder.getFromLocation(lat, lon, 1)[0]
            .locality
            .filter { it.isLetter() }


    private fun makeToast(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()


    override fun onDestroyView() {
        super.onDestroyView()
        locationService.stopSelf()
        _binding = null
    }
}
