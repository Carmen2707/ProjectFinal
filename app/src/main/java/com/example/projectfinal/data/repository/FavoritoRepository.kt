package com.example.projectfinal.data.repository

import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.UiState

interface FavoritoRepository {
    fun cargarFavoritos(usuario: Usuario?, result: (UiState<List<Restaurante>>) -> Unit)

    fun addFavorito(restaurante: Restaurante, usuarioId: String, result: (UiState<Unit>) -> Unit)
}