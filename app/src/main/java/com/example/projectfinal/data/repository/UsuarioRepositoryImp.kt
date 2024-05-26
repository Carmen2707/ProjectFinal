package com.example.projectfinal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectfinal.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth

class UsuarioRepositoryImp(
    val auth: FirebaseAuth
) : UsuarioRepository {
    override fun obtenerUsuario(userId: String): Usuario? {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            Usuario(
                nombre = currentUser.displayName ?: "",
                email = currentUser.email ?: ""
            )
        } else {
            null
        }
    }

    override fun getCurrentUser(): LiveData<Usuario?> {
        val currentUserLiveData = MutableLiveData<Usuario?>()

        auth.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val usuario = Usuario(
                    currentUser.displayName ?: "",
                    "",
                    currentUser.email ?: ""
                )
                currentUserLiveData.value = usuario
            } else {
                currentUserLiveData.value = null
            }
        }
        return currentUserLiveData
    }
}
