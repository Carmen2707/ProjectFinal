package com.example.projectfinal.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectfinal.data.model.Restaurante

@Dao
interface DAO {
    @Query("SELECT * FROM restaurantesBD")
    fun getAll(): List<Restaurante>


    @Update
    fun actualizarRestaurante(restaurante: Restaurante)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurante: MutableList<Restaurante>)

    @Query("SELECT * FROM restaurantesBD WHERE favorito = 1")
    fun getFavoritos(): List<Restaurante>
}