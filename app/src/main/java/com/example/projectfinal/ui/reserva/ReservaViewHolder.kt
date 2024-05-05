package com.example.projectfinal.ui.reserva

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva

class ReservaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNombreRestaurante: TextView = view.findViewById(R.id.tvNombreRestaurante)
    private val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    private val tvNumPersonas: TextView = view.findViewById(R.id.tvNumPersonas)
    private val tvObservaciones: TextView = view.findViewById(R.id.tvObservaciones)
    val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
    val btnBorrar: ImageButton = view.findViewById(R.id.btnBorrar)


    fun render(reserva: Reserva) {
        tvNombreRestaurante.text = reserva.restaurante
        tvFecha.text = reserva.fecha.toString()
        tvNumPersonas.text = reserva.personas.toString()
        tvObservaciones.text = reserva.observaciones

    }
}