package com.example.projectfinal.data.model

data class Restaurante(
    val nombre: String? = null,
    val direccion: String? = null,
    val horario: String? = null,
    val contacto: Long? = null,
    val imagen: String? = null,
    val categoria: String? = null,
    var seleccionada: Boolean
) {

}
