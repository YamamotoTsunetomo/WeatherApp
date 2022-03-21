package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentXmlBinding

class XmlFragment : Fragment() {

    private lateinit var _binding: FragmentXmlBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var recycler: RecyclerView

    private val weatherAdapter by lazy {
        WeatherAdapter(layoutInflater, GlideImageLoader(requireContext()))
    }

    val binding: FragmentXmlBinding
        get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentXmlBinding.inflate(inflater, container, false)
        recycler = binding.recyclerView
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)



        recycler.alpha = if (viewModel.isRecyclerVisible) 1.0f else .0f
        viewModel.isRecyclerVisible = true
        recycler.adapter = weatherAdapter
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.btnGetWeathers.setOnClickListener {
            recycler.animate().alpha(1.0f)
            weatherAdapter.setData(viewModel.weathers, viewLifecycleOwner)
        }

        binding.btnRestart.setOnClickListener {
            viewModel.restartWeathers()
            weatherAdapter.setData(viewModel.weathers, viewLifecycleOwner)
        }


        return binding.root
    }

}