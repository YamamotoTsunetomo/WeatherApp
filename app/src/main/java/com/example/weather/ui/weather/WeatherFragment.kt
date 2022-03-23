package com.example.weather.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.extensions.GlideImageLoader
import com.example.weather.ui.weather.adapter.WeatherAdapter
import com.example.weather.ui.weather.vm.WeatherViewModel
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.network.WeatherApiServiceObject

class WeatherFragment : Fragment() {

    private lateinit var _binding: FragmentWeatherBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var recycler: RecyclerView

    private val weatherAdapter by lazy {
        WeatherAdapter(layoutInflater, GlideImageLoader(requireContext()), requireContext())
    }

    val binding: FragmentWeatherBinding
        get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = weatherAdapter

        viewModel.setWeathers(WeatherApiServiceObject.CITIES)
        viewModel.weathers.observe(viewLifecycleOwner) {
                weatherAdapter.setData(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        recycler = binding.recyclerView
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)



        return binding.root
    }
}
