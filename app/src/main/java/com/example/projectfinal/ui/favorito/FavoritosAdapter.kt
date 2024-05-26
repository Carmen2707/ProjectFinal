package com.example.projectfinal.ui.favorito

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class FavoritosAdapter(
    private var favoritos: MutableList<Restaurante> = mutableListOf(),
    private val onFavoritoChangeListener: (Restaurante, Boolean) -> Unit,
    private val onItemSelected: (restaurante: Restaurante) -> Unit
) :
    RecyclerView.Adapter<FavoritosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_restaurante, parent, false)
        return FavoritosViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritosViewHolder, position: Int) {
        val restaurante = favoritos[position]
        holder.bind(restaurante, onItemSelected)
        holder.checkBox.isChecked = restaurante.favorito == true
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onFavoritoChangeListener(restaurante, isChecked)
            if (!isChecked) {
                favoritos.remove(restaurante)
                notifyItemRemoved(position)
            }
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