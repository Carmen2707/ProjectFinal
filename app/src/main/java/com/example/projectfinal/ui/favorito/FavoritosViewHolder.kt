package com.example.projectfinal.ui.favorito

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Favorito
import com.example.projectfinal.data.model.Restaurante

class FavoritosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivRestaurante: ImageView = itemView.findViewById(R.id.ivRestaurante)
    private val tvNombreRestaurante: TextView = itemView.findViewById(R.id.tvNombreRestaurante)
    private val tvDireccionRestaurante: TextView = itemView.findViewById(R.id.tvDireccionRestaurante)
    private val tvHorarioRestaurante: TextView = itemView.findViewById(R.id.tvHorarioRestaurante)
    private val tvContactoRestaurante: TextView = itemView.findViewById(R.id.tvContactoRestaurante)

    fun bind(restaurante: Restaurante) {
        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .into(ivRestaurante)
        tvNombreRestaurante.text = restaurante.nombre
        tvDireccionRestaurante.text = restaurante.direccion
        tvHorarioRestaurante.text = restaurante.horario
        tvContactoRestaurante.text = restaurante.contacto.toString()

    }
}