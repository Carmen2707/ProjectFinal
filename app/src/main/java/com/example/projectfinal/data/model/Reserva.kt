package com.example.projectfinal.data.model

import java.util.Date

data class Reserva (var fecha: Date?=null, val usuario: String?=null, val observaciones:String?=null, val personas:Int?=null, val restaurante: String?=null)