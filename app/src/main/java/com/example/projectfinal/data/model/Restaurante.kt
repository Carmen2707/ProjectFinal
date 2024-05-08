package com.example.projectfinal.data.model

import androidx.room.PrimaryKey


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
    var favorito: Boolean = false,
    var userId: String? = null,
    var idFavorito: String? = null
) {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(
        id = 0,
        nombre = "",
        categoria = "",
        horaApertura = "",

        horaCierre = ""
    )
}

