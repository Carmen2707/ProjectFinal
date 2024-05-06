package com.example.projectfinal.data.repository

import androidx.lifecycle.LiveData
import com.example.projectfinal.data.model.Usuario

interface UsuarioRepository {

    fun getCurrentUser():LiveData<Usuario?>

}