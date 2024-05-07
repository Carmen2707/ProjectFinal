package com.example.projectfinal.data.model

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "favoritos")
data class Favorito(
    @PrimaryKey(autoGenerate = true) val id: String,
    @ColumnInfo(name = "usuario") val usuario: String,
    val nombre: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val contacto: Long? = null,
    val imagen: String? = null,
    val categoria: String? = null,
    val favorito: Boolean? = null
)

