package com.example.projectfinal.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projectfinal.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class UsuarioRepositoryImp(
    val auth: FirebaseAuth,
    val appPreferences: SharedPreferences,
    val gson: Gson
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

    override fun getUsuario(): Usuario? {
        // Obtener los datos del usuario almacenados en SharedPreferences
        val usuarioJson = appPreferences.getString("usuario", null)

        // Convertir el JSON a un objeto de la clase Usuario utilizando Gson
        return if (usuarioJson != null) {
            gson.fromJson(usuarioJson, Usuario::class.java)
        } else {
            null
        }
    }

    override fun getCurrentUser(): LiveData<Usuario?> {
        val currentUserLiveData = MutableLiveData<Usuario?>()

        // Observa los cambios en el estado de autenticación del usuario
        auth.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // El usuario está autenticado, crea un objeto Usuario con su información
                val usuario = Usuario(
                    currentUser.displayName ?: "",
                    "",
                    currentUser.email ?: ""
                )
                currentUserLiveData.value = usuario
            } else {
                // El usuario no está autenticado, establece el valor en null
                currentUserLiveData.value = null
            }
        }

        return currentUserLiveData
    }
}
