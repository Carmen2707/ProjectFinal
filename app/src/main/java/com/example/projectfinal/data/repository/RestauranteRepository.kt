package com.example.projectfinal.data.repository

import com.example.projectfinal.data.model.Restaurante

interface RestauranteRepository {
    fun crearRestaurante(restaurante: Restaurante)
    fun borrarRestaurante(
        position: Int,
        restaurantes: List<Restaurante>,
        callback: (Boolean) -> Unit
    )
}