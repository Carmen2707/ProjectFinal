package com.example.projectfinal.ui.reserva

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.UsuarioViewModel
import com.example.projectfinal.databinding.FragmentMisReservasBinding

import com.example.projectfinal.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MisReservasFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentMisReservasBinding
    val viewModelReserva: ReservaViewModel by viewModels()
    val viewModelUsuario: UsuarioViewModel by viewModels()
    val adapter by lazy { ReservaAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
         //   param1 = it.getString(ARG_PARAM1)
          //  param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMisReservasBinding.bind(view)
        // Obtener el usuario actualmente autenticado
        viewModelUsuario.getSession().observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                Log.d("MisReservasFragment", "Usuario autenticado: $usuario")

                viewModelReserva.cargarReservas(usuario)

            } ?: run {
                Log.d("MisReservasFragment", "Usuario no autenticado")
            }
        }
        viewModelReserva.reserva.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {

                }
                is UiState.Success -> {
                    // Actualizar el adaptador con la lista de reservas
                    adapter.updateList(uiState.data.toMutableList())
                }


                else -> {}
            }
        }
        binding.rvReservas.adapter = adapter
        binding.rvReservas.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMisReservasBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MisReservasFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MisReservasFragment().apply {
                arguments = Bundle().apply {
                  //  putString(ARG_PARAM1, param1)
                 //   putString(ARG_PARAM2, param2)
                }
            }
    }
}