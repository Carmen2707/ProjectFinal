package com.example.projectfinal.data.model

import java.util.Date

data class Reserva(
    var fecha: String = "",
    var hora: String = "",
    var usuario: String = "",
    var observaciones: String? = null,
    var personas: Int = 0,
    var restaurante: String = ""
) {
    constructor() : this("", "", "", null, 0, "")
}

