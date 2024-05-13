package com.example.projectfinal.data.model

data class Reserva(
    var id: String = "",
    var fecha: String = "",
    var hora: String = "",
    var usuario: String = "",
    var nombreUsuario: String = "",
    var observaciones: String? = null,
    var personas: Int = 0,
    var restaurante: String = "",
    var horaApertura: String = "",
    var horaCierre: String = ""
) {
    constructor() : this("", "", "", "", "",null, 0, "")
}

