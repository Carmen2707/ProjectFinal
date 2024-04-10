package com.example.projectfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.data.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class UsuarioViewModel @Inject constructor(val repository: UsuarioRepository
): ViewModel()  {
    fun getSession(): LiveData<Usuario?> {
      return  repository.getCurrentUser()
    }
}