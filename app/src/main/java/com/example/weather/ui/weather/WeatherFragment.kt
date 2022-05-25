package com.example.weather.ui.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherBinding
import com.example.weather.ui.weather.adapter.WeatherAdapter
import com.example.weather.ui.weather.vm.WeatherViewModel
import com.example.weather.util.GlideImageLoader
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private val disposables = mutableListOf<Disposable>()
    private var _binding: FragmentWeatherBinding? = null

    private val weatherViewModel: WeatherViewModel by viewModel()
    private var hasLoaded = false

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
        initRecycler()
        handleRequestLoadingScreen()
        initObservers()

        binding.btnNavigateToEdit.setOnClickListener {
            findNavController().navigate(R.id.action_weatherFragment_to_addCityFragment)
        }
    }

    private fun handleRequestLoadingScreen() {
        if (!weatherViewModel.hasBeenSet) {
//            lifecycleScope.launch {
            if (isNetworkActive()) {
//                    launch {
                disposables.add(
                    weatherViewModel.setWeathersFromApiAndStoreToDatabase()
                )
//                Thread.sleep(700)
            } else {
                disposables.add(
                    weatherViewModel.setWeathersFromDatabase()
                )
                makeToast(requireContext(), "No Internet. Data may be outdated")
                weatherAdapter.setData(weatherViewModel.weathers.value!!)
            }
            stopLoading()
            weatherViewModel.handleSet(true)
        }
    }

    private fun initObservers() {
        weatherViewModel.currentAddedItem.observe(viewLifecycleOwner) {
            if (it.first) {
                weatherAdapter.addItem(it.second!!)
                weatherViewModel.setCurrentItem(Pair(false, null))
                weatherViewModel.handleAdd(false)
            }

            if (weatherAdapter.itemCount == 0)
                weatherAdapter.setData(weatherViewModel.weathers.value!!)
        }
    }

    private fun initRecycler() {
        if (weatherViewModel.hasBeenSet)
            stopLoading()
        recycler = binding.recyclerView
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recycler.adapter = weatherAdapter
        setOnSwipeDelete()
    }

    private fun stopLoading() {
        hasLoaded = true
        binding.tvLoading.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.btnNavigateToEdit.visibility = View.VISIBLE
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
                disposables.add(
                    weatherViewModel.removeWeather(position)
                )
                weatherAdapter.removeItem(position)
                weatherViewModel.handleRemove(false)
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    private fun isNetworkActive(): Boolean {
        val connectivityManager = (requireActivity()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager
                .getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            if (connectivityManager.activeNetworkInfo != null &&
                connectivityManager.activeNetworkInfo!!
                    .isConnectedOrConnecting
            )
                return true
        }
        return false
    }


    private fun makeToast(context: Context, text: String) =
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.forEach { if (!it.isDisposed) it.dispose() }
    }

}
