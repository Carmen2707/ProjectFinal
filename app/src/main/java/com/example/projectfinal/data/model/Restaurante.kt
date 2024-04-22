package com.example.projectfinal.data.model

import com.example.projectfinal.ui.categoria.Categorias


data class Restaurante(
    val nombre: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val contacto: Long? = null,
    val imagen: String? = null,
    val categoria: String,
    var seleccionada: Boolean
)
