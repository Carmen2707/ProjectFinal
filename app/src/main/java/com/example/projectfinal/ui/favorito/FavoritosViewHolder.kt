package com.example.projectfinal.ui.favorito

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class FavoritosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivRestaurante: ImageView = itemView.findViewById(R.id.ivRestaurante)
    private val tvNombreRestaurante: TextView = itemView.findViewById(R.id.tvNombreRestaurante)
    private val tvDireccionRestaurante: TextView =
        itemView.findViewById(R.id.tvDireccionRestaurante)
    private val tvHorarioRestaurante: TextView = itemView.findViewById(R.id.tvHorarioRestaurante)
    private val tvContactoRestaurante: TextView = itemView.findViewById(R.id.tvContactoRestaurante)
    val checkBox: CheckBox = itemView.findViewById(R.id.cbFavorito)
    private val tvApertura: TextView = itemView.findViewById(R.id.tvApertura)
    private val tvCierre: TextView = itemView.findViewById(R.id.tvCierre)
    fun bind(restaurante: Restaurante) {
        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .into(ivRestaurante)
        tvNombreRestaurante.text = restaurante.nombre
        tvDireccionRestaurante.text = restaurante.direccion
        tvHorarioRestaurante.text = restaurante.horario
        tvContactoRestaurante.text = restaurante.contacto.toString()
        tvApertura.text = restaurante.horaApertura
        tvCierre.text = restaurante.horaCierre
        // Actualizar el estado del CheckBox
        checkBox.isChecked = restaurante.favorito
    }
}