package com.example.projectfinal.ui.favorito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Restaurante

class FavoritosAdapter(
    var favoritos:  MutableList<Restaurante> = mutableListOf(),
    private val onFavoritoChangeListener: (Restaurante, Boolean) -> Unit
) :
    RecyclerView.Adapter<FavoritosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_restaurante, parent, false)
        return FavoritosViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritosViewHolder, position: Int) {
        val restaurante = favoritos[position]
        holder.bind(restaurante)
        holder.checkBox.isChecked = restaurante.favorito == true
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onFavoritoChangeListener(restaurante, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return favoritos.size
    }

    fun updateList(lista: MutableList<Restaurante>) {
        this.favoritos.clear()
        this.favoritos.addAll(lista)
        notifyDataSetChanged()
    }
}