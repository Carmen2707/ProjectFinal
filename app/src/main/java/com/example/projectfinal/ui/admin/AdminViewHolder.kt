package com.example.projectfinal.ui.admin

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class AdminViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    private val ivRestaurante: ImageView = view.findViewById(R.id.ivRestaurante)
    private val tvNombreRestaurante: TextView = view.findViewById(R.id.tvNombreRestaurante)
    private val tvDireccionRestaurante: TextView = view.findViewById(R.id.tvDireccionRestaurante)
    private val tvHorarioRestaurante: TextView = view.findViewById(R.id.tvHorarioRestaurante)
    private val tvContactoRestaurante: TextView = view.findViewById(R.id.tvContactoRestaurante)
    private val tvApertura: TextView = view.findViewById(R.id.tvApertura)
    private val tvCierre: TextView = view.findViewById(R.id.tvCierre)

    fun render(restaurante: Restaurante, onClickListener: (Restaurante) -> Unit) {
        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .into(ivRestaurante)
        tvNombreRestaurante.text = restaurante.nombre
        tvDireccionRestaurante.text = restaurante.direccion
        tvHorarioRestaurante.text = restaurante.horario
        tvContactoRestaurante.text = restaurante.contacto.toString()
        tvApertura.text = restaurante.horaApertura
        tvCierre.text = restaurante.horaCierre
        itemView.setOnClickListener {
            onClickListener(restaurante)
        }
    }
}