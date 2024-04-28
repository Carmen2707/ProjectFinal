package com.example.projectfinal.ui.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class RestauranteAdapter(var restaurantes: MutableList<Restaurante>, private val onFavoritoChangeListener: (Restaurante, Boolean) -> Unit/*, private val onItemSelected: (Int) -> Unit*/) :
    RecyclerView.Adapter<RestauranteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurante, parent, false)
        return RestauranteViewHolder(view,  onFavoritoChangeListener)
    }

    override fun getItemCount(): Int = restaurantes.size

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val restaurante = restaurantes[position]
        holder.render(restaurante)
        holder.checkBox.isChecked = restaurante.favorito == true
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onFavoritoChangeListener(restaurante, isChecked)
        }
    }





}