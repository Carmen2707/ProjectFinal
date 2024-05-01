package com.example.projectfinal.ui.formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.projectfinal.R
import com.example.projectfinal.databinding.FragmentAnadirReservaBinding
import com.example.projectfinal.databinding.FragmentMisReservasBinding
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
    //    binding.tvTitulo.text = args.restauranteNombre
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FormularioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FormularioFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}