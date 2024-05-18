package com.example.projectfinal.ui.admin

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Restaurante

class ReservasAdminViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    private val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    private val tvHora: TextView = view.findViewById(R.id.tvHora)
    private val tvNumPersonas: TextView = view.findViewById(R.id.tvPersonas)
    private val tvObservaciones: TextView = view.findViewById(R.id.tvObservaciones)
    private val tvCorreo: TextView = view.findViewById(R.id.tvCorreo)
    private val tvResponsable: TextView = view.findViewById(R.id.tvResponsable)
    val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
    val btnBorrar: ImageButton = view.findViewById(R.id.btnBorrar)
    fun render(reserva: Reserva) {

        tvFecha.text = reserva.fecha
        tvHora.text = reserva.hora
        tvNumPersonas.text = reserva.personas.toString()
        tvObservaciones.text = reserva.observaciones
        tvCorreo.text = reserva.usuario
        tvResponsable.text = reserva.nombreUsuario

    }
}