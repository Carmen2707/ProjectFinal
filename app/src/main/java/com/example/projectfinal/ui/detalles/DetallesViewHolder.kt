package com.example.projectfinal.ui.detalles

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.projectfinal.R

class DetallesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val carouselImageView: AppCompatImageView = view.findViewById(R.id.carouselImageView)
    fun bind(imagenUrl: String) {
        carouselImageView.load(imagenUrl) {
            transformations(RoundedCornersTransformation(80f, 80f, 80f, 80f))
        }
    }
}