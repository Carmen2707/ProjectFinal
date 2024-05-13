package com.example.projectfinal.ui.categoria

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Categorias

class CategoriaAdapter(
    private val categorias: List<Categorias>,
    private val onItemSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<CategoriasViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_categoria, parent, false)
        return CategoriasViewHolder(view)
    }

    override fun getItemCount(): Int = categorias.size

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
        holder.render(
            categorias[position], onItemSelected

        )
    }

    fun resetearCategorias() {
        for (categoria in categorias) {
            categoria.seleccionada = false
        }
        notifyDataSetChanged()
    }
}