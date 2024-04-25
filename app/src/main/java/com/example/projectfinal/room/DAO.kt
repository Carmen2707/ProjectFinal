package com.example.projectfinal.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectfinal.data.model.Favorito
import com.example.projectfinal.data.model.Restaurante

@Dao
interface DAO {
    @Query("SELECT * FROM restaurantesBD")
    fun getAll(): LiveData<List<Restaurante>>


    @Update
    fun actualizarRestaurante(restaurante: Restaurante)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurante: MutableList<Restaurante>)

    @Query("SELECT * FROM favoritos WHERE userId = :userId")
    fun getFavoritos(userId: String): LiveData<List<Restaurante>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorito(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE restauranteId = :restauranteId")
    suspend fun eliminarFavoritoByRestauranteId(restauranteId: Long)

}