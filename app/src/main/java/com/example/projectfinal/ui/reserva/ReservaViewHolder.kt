package com.example.projectfinal.ui.reserva

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva

class ReservaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNombreRestaurante: TextView = view.findViewById(R.id.tvNombreRestaurante)
    private val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    private val tvNumPersonas: TextView = view.findViewById(R.id.tvNumPersonas)
    private val tvObservaciones: TextView = view.findViewById(R.id.tvObservaciones)
    fun render(reserva: Reserva) {
       tvNombreRestaurante.text = reserva.restaurante.toString()
        tvFecha.text = reserva.fecha.toString()
        tvNumPersonas.text = reserva.personas.toString()
       tvObservaciones.text = reserva.observaciones
        // También puedes agregar un click listener si lo necesitas
        //  itemView.setOnClickListener { onItemClick(reserva) }
    }
}