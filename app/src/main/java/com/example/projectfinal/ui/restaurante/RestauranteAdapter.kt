package com.example.projectfinal.ui.restaurante

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante

class RestauranteAdapter(
    private val onFavoritoChangeListener: (Restaurante, Boolean) -> Unit,
    private val onItemSelected: (restaurante: Restaurante) -> Unit
) :
    ListAdapter<Restaurante, RestauranteViewHolder>(RESTAURANTE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_restaurante, parent, false)
        return RestauranteViewHolder(view, onFavoritoChangeListener)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val restaurante = currentList[position]
        holder.render(restaurante, onItemSelected)

        holder.checkBox.isChecked = restaurante.favorito


        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onFavoritoChangeListener(restaurante, isChecked)
        }
    }
    fun updateFavoritos(favoritos: List<Restaurante>) {
        currentList.forEach { restaurante ->
            restaurante.favorito = favoritos.any { it.id == restaurante.id }
        }
        notifyDataSetChanged()
    }
    companion object {
        val RESTAURANTE_COMPARATOR = object : DiffUtil.ItemCallback<Restaurante>() {
            override fun areItemsTheSame(oldItem: Restaurante, newItem: Restaurante): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Restaurante, newItem: Restaurante): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}