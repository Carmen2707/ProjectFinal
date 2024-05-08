package com.example.projectfinal.data.model

data class Usuario(
    var nombre: String = "",
    var password: String = "",
    var email: String = "",
    var favoritos: ArrayList<Restaurante> = arrayListOf()
)
