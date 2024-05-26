package com.example.projectfinal.ui.detalles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R

class DetallesAdapter(
    val imagenes: List<String>
) :
    RecyclerView.Adapter<DetallesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetallesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return DetallesViewHolder(view)
    }

    override fun getItemCount(): Int = imagenes.size
    override fun onBindViewHolder(holder: DetallesViewHolder, position: Int) {
        holder.bind(imagenes[position])
    }
}