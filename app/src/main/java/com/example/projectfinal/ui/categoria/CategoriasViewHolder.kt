package com.example.projectfinal.ui.categoria

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R

class CategoriasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val viewContainer: CardView = view.findViewById(R.id.viewContainer)
    private val ivCategorias: ImageView = view.findViewById(R.id.ivCategoria)
    private val tvNombreCategoria: TextView = view.findViewById(R.id.tvNombreCategoria)
    fun render(categorias: Categorias, onItemSelected: (Int) -> Unit) {
        val color = if (categorias.seleccionada) {
            R.color.gray
        } else {
            R.color.white
        }
        viewContainer.setCardBackgroundColor(ContextCompat.getColor(viewContainer.context, color))
        itemView.setOnClickListener { onItemSelected(layoutPosition) }
        when (categorias) {
            Categorias.Italiano -> {
                tvNombreCategoria.text = "Italiano"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.pizza)
                )
            }

            Categorias.Indio -> {
                tvNombreCategoria.text = "Indio"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.curry)
                )
            }

            Categorias.China -> {
                tvNombreCategoria.text = "China"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.china)
                )
            }

            Categorias.Japonesa -> {
                tvNombreCategoria.text = "Japonesa"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.sushi)
                )
            }

            Categorias.Mediterránea -> {
                tvNombreCategoria.text = "Mediterránea"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.vegetarian)
                )
            }

            Categorias.Tapas -> {
                tvNombreCategoria.text = "Tapas"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.tapas)
                )
            }

            Categorias.Mexicano -> {
                tvNombreCategoria.text = "Mexicano"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.mexican)
                )
            }

            Categorias.Cafés -> {
                tvNombreCategoria.text = "Cafés"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.coffee)
                )
            }

            Categorias.Vegetariano -> {
                tvNombreCategoria.text = "Vegetariano"
                ivCategorias.setImageDrawable(
                    ContextCompat.getDrawable(ivCategorias.context, R.drawable.vegetarian)
                )
            }

            else -> {}
        }
    }
}