package com.example.projectfinal.data.model

import androidx.room.PrimaryKey
import java.io.Serializable


data class Restaurante(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val nombre: String,
    val direccion: String? = null,
    val horario: String? = null,
    val horaApertura: String,
    val horaCierre: String,
    val contacto: Long? = null,
    val imagen: String? = null,
    val categoria: String,
    var imagenes: List<String>,
    var web: String? = null,
    var favorito: Boolean? = null,
) : Serializable {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(
        id = 0,
        nombre = "",
        categoria = "",
        horaApertura = "",
        horaCierre = "",
        imagenes = listOf<String>()
    )
}

