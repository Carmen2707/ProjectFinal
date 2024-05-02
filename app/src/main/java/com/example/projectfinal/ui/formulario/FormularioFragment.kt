package com.example.projectfinal.ui.formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projectfinal.R
import com.example.projectfinal.databinding.FragmentAnadirReservaBinding
import com.example.projectfinal.databinding.FragmentMisReservasBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormularioFragment : Fragment() {
    val args: FormularioFragmentArgs by navArgs()
    private lateinit var binding: FragmentAnadirReservaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnadirReservaBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnadirReservaBinding.bind(view)
        binding.tvTitulo.text = args.restauranteNombre
        binding.btnCerrar.setOnClickListener {
            findNavController().navigate(FormularioFragmentDirections.actionFormularioFragmentToRestaurantesFragment())
        }
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FormularioFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}