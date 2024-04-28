package com.example.projectfinal.ui.restaurante

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class RestauranteViewHolder(view: View, private val onFavoritoChangeListener: (Restaurante, Boolean) -> Unit) : RecyclerView.ViewHolder(view) {
    private val ivRestaurante: ImageView = view.findViewById(R.id.ivRestaurante)
    private val tvNombreRestaurante: TextView = view.findViewById(R.id.tvNombreRestaurante)
    private val tvDireccionRestaurante: TextView = view.findViewById(R.id.tvDireccionRestaurante)
    private val tvHorarioRestaurante: TextView = view.findViewById(R.id.tvHorarioRestaurante)
    private val tvContactoRestaurante: TextView = view.findViewById(R.id.tvContactoRestaurante)
    val checkBox: CheckBox = view.findViewById(R.id.cbFavorito)
    private lateinit var restaurante: Restaurante
    init {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Llamar al listener con el restaurante y su estado de favorito actualizado
            if (::restaurante.isInitialized) {
                onFavoritoChangeListener.invoke(restaurante, isChecked)
            }
        }
    }

    fun render(restaurante: Restaurante) {
        Log.d("RestauranteViewHolder", "Cargando imagen desde URL: ${restaurante.imagen}")

        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .into(ivRestaurante)
        tvNombreRestaurante.text = restaurante.nombre
        tvDireccionRestaurante.text = restaurante.direccion
        tvHorarioRestaurante.text = restaurante.horario
        tvContactoRestaurante.text = restaurante.contacto.toString()

        // Actualizar el estado del CheckBox
        checkBox.isChecked = restaurante.favorito ?: false

    }
}