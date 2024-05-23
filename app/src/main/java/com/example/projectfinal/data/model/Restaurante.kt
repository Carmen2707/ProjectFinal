package com.example.projectfinal.data.model

import java.io.Serializable


data class Restaurante(
    var id: Long = 0,
    val nombre: String,
    val direccion: String? = null,
    val horario: String? = null,
    val horaApertura: String,
    val horaCierre: String,
    val contacto: Long? = null,
    var imagen: String? = null,
    val categoria: String,
    var imagenes: List<String>,
    var web: String? = null,
    var favorito: Boolean? = null,
) : Serializable {
    // Constructor sin argumentos requerido por Firestore
    constructor() : this(
        id = 0,
        nombre = "",
        direccion = "",
        horario = "Todos los dias de",
        categoria = "",
        horaApertura = "",
        horaCierre = "",
        imagen = "",
        contacto = 0,
        imagenes = listOf<String>()
    )
}

