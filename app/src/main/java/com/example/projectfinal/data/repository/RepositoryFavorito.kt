package com.example.projectfinal.data.repository

import androidx.lifecycle.LiveData
import com.example.projectfinal.data.model.Favorito
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.room.DAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryFavorito(private val dao: DAO) {

    fun getAllRestaurantes(): LiveData<List<Restaurante>> {
        return dao.getAll()
    }

    suspend fun insertFavorito(favorito: Favorito) {
        withContext(Dispatchers.IO) {
            dao.insertFavorito(favorito)
        }
    }

    suspend fun eliminarFavoritoByRestauranteId(restauranteId: Long) {
        dao.eliminarFavoritoByRestauranteId(restauranteId)
    }

    fun getFavoritos(userId: String): LiveData<List<Restaurante>> {
        return dao.getFavoritos(userId)
    }
}
