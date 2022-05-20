package com.example.weather.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.ui.weather.adapter.WeatherAdapter
import com.example.weather.ui.weather.vm.WeatherViewModel
import com.example.weather.util.GlideImageLoader
import kotlinx.coroutines.launch
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = binding.recyclerView
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = weatherAdapter

        setOnSwipeDelete()

        if (!weatherViewModel.hasBeenHandled) {
            lifecycleScope.launch {
                weatherViewModel.setWeathersFromApiToDatabase()
                weatherViewModel.handle()
            }
        }


        weatherViewModel.weathers.observe(viewLifecycleOwner) {
            if (!weatherViewModel.hasItemBeenRemoved) {
                Log.d(
                    "TAGTAGTAG",
                    "aaaaa${weatherViewModel.weathers.value!!.map { it.locationName }}"
                )
                weatherAdapter.setData(weatherViewModel.weathers.value!!)
            }
        }

        binding.btnNavigateToEdit.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_go_to_edit)
        }

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
                weatherViewModel.removeWeather(position)
                weatherAdapter.removeItem(position)
                weatherViewModel.hasItemBeenRemoved = false
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
