package com.example.projectfinal.ui.detalles

import android.view.RoundedCorner
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView

class DetallesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val carouselImageView: AppCompatImageView = view.findViewById(R.id.carouselImageView)

    fun bind(imagenUrl: String) {
        carouselImageView.load(imagenUrl) {
            transformations(RoundedCornersTransformation(80f,80f,80f,80f))
        }

    }
}