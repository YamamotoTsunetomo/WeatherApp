package com.example.weather.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentEditCitiesBinding
import com.example.weather.ui.edit.vm.EditCitiesViewModel

class EditCitiesFragment : Fragment() {

    private lateinit var _binding: FragmentEditCitiesBinding

    private lateinit var viewModel: EditCitiesViewModel

    val binding: FragmentEditCitiesBinding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCitiesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(EditCitiesViewModel::class.java)

        binding.btnAdd.setOnClickListener {
            viewModel.btnAddOnClickListener(
                binding.etField,
                requireContext()
            )
        }

        binding.btnRemove.setOnClickListener {
            viewModel.btnRemoveOnClickListener(
                binding.etField,
                requireContext()
            )
        }

        return binding.root
    }

}
