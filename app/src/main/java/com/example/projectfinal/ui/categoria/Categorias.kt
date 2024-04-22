package com.example.projectfinal.ui.categoria

sealed class Categorias(var seleccionada: Boolean = false) {
    object Italiano : Categorias()
    object Indio : Categorias()
    object China : Categorias()
    object Japonesa : Categorias()
    object Mediterránea : Categorias()
    object Tapas : Categorias()

    object Mexicano : Categorias()
    object Cafés : Categorias()
    object Vegetariano : Categorias()
}