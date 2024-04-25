package com.example.projectfinal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "restaurantesBD")
data class Restaurante(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val contacto: Long? = null,
    val imagen: String? = null,
    val categoria: String,
    var favoritos: List<Restaurante>? = null
)
