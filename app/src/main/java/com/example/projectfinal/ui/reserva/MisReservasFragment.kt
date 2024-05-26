package com.example.projectfinal.ui.reserva

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.databinding.FragmentMisReservasBinding
import com.example.projectfinal.ui.auth.UsuarioViewModel
import com.example.projectfinal.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MisReservasFragment : Fragment() {
    private lateinit var binding: FragmentMisReservasBinding
    val viewModelReserva: ReservaViewModel by viewModels()
    private val viewModelUsuario: UsuarioViewModel by viewModels()
    val adapter = ReservaAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMisReservasBinding.bind(view)
        viewModelUsuario.getSession().observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                viewModelReserva.cargarReservas(usuario)

            } ?: run {
                Log.d("MisReservasFragment", "Usuario no autenticado")
            }
        }
        viewModelReserva.reserva.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
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

        adapter.setOnItemClickListener(object : ReservaAdapter.OnItemClickListener {
            override fun onEditarClick(position: Int) {
                val reserva = adapter.getItemAtPosition(position)
                val action =
                    MisReservasFragmentDirections.actionMisReservasFragmentToFormularioFragment(
                        restauranteNombre = reserva.restaurante,
                        id = reserva.id,
                        nombreUsuario = reserva.nombreUsuario,
                        personas = reserva.personas,
                        fecha = reserva.fecha,
                        hora = reserva.hora,
                        observaciones = reserva.observaciones,
                        horaApertura = reserva.horaApertura,
                        horaCierre = reserva.horaCierre,
                        isEdit = true
                    )
                findNavController().navigate(action)
            }


            override fun onBorrarClick(position: Int) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("¿Seguro que quieres eliminar esta reserva?")
                builder.setPositiveButton("Eliminar") { dialog, _ ->
                    viewModelReserva.borrarReserva(position) { success ->
                        if (success) {
                            adapter.notifyItemRemoved(position)
                        }
                    }
                    dialog.dismiss()
                }
                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setCancelable(false)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMisReservasBinding.inflate(inflater, container, false)
        return binding.root
    }
}