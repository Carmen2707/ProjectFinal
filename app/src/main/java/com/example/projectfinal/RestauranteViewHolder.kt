package com.example.projectfinal

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RestauranteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ivRestaurante: ImageView = view.findViewById(R.id.ivRestaurante)
    private val tvNombreRestaurante: TextView = view.findViewById(R.id.tvNombreRestaurante)
    private val tvDireccionRestaurante: TextView = view.findViewById(R.id.tvDireccionRestaurante)
    private val tvHorarioRestaurante: TextView = view.findViewById(R.id.tvHorarioRestaurante)
    private val tvContactoRestaurante: TextView = view.findViewById(R.id.tvContactoRestaurante)

    fun render(restaurante: Restaurante) {
        Log.d("RestauranteViewHolder", "Cargando imagen desde URL: ${restaurante.imagen}")

        Glide.with(itemView.context)
            .load(restaurante.imagen)
            .into(ivRestaurante)
        tvNombreRestaurante.text = restaurante.nombre
        tvDireccionRestaurante.text = restaurante.direccion
        tvHorarioRestaurante.text = restaurante.horario
        tvContactoRestaurante.text= restaurante.contacto.toString()

        Log.d("RestauranteViewHolder", "Nombre: ${restaurante.nombre}")
        Log.d("RestauranteViewHolder", "Dirección: ${restaurante.direccion}")
        Log.d("RestauranteViewHolder", "Horario: ${restaurante.horario}")
        Log.d("RestauranteViewHolder", "Contacto: ${restaurante.contacto}")
    }
}