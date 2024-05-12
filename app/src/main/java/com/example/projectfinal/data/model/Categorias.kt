package com.example.projectfinal.data.model


sealed class Categorias(var seleccionada: Boolean = false) {
    object Italiano : Categorias()
    object Indio : Categorias()
    object Chino : Categorias()
    object Japones : Categorias()
    object Mediterráneo : Categorias()
    object Tapas : Categorias()

    object Mexicano : Categorias()
    object Cafés : Categorias()
    object Vegetariano : Categorias()
}