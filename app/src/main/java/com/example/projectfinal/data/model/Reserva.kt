package com.example.projectfinal.data.model

import java.util.Date

data class Reserva(
    var fecha: Date? = null,
    var usuario: String ,
    var observaciones: String? = null,
    var personas: Int? = null,
    var restaurante: String = ""
)
