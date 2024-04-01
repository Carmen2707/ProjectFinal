package com.example.projectfinal

data class Restaurante(val nombre:String, val direccion:String, val horario:String, val contacto:Int,
                       val categoria:Categorias, val seleccionada:Boolean = false) {
}