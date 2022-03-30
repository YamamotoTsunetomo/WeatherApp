package com.example.weather.ui.edit

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentEditCitiesBinding
import com.example.weather.ui.edit.vm.EditCitiesViewModel

class EditCitiesFragment : Fragment() {

    private var _binding: FragmentEditCitiesBinding? = null

    private lateinit var viewModel: EditCitiesViewModel


    val binding: FragmentEditCitiesBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditCitiesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(EditCitiesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val cityFieldText = binding.etField.text

            if (hasFilled(cityFieldText)) {
                viewModel.addCity(cityFieldText.toString())
                cityFieldText.clear()
            } else showToast()
        }

        binding.btnRemove.setOnClickListener {
            val cityFieldText = binding.etField.text

            if (hasFilled(cityFieldText)) {
                if (!viewModel.existsCity(cityFieldText.toString()))
                    showToast(getString(R.string.no_such_city_toast))
                else {
                    viewModel.removeCity(cityFieldText.toString())
                    cityFieldText.clear()
                }
            } else showToast()
        }

        binding.btnGoBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_go_to_weathers)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(msg: String = getString(R.string.fill_the_field_toast)) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    private fun hasFilled(et: Editable) = et.isNotBlank()


}
