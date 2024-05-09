package com.example.projectfinal.data.repository

import android.util.Log
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.FireStoreCollection
import com.example.projectfinal.util.FireStoreDocumentField
import com.example.projectfinal.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class FavoritoRepositoryImp(val database: FirebaseFirestore) : FavoritoRepository {
    val documento = database.collection(FireStoreCollection.FAVORITOS).document()

    override fun cargarFavoritos(usuario: Usuario?, result: (UiState<List<Restaurante>>) -> Unit) {
        if (usuario != null) {
            database.collection(FireStoreCollection.FAVORITOS)
                .whereEqualTo(FireStoreDocumentField.USER_ID, usuario.email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val favoritos = arrayListOf<Restaurante>()
                    for (document in querySnapshot) {
                        val restaurante = document.toObject(Restaurante::class.java)
                      //  restaurante.idFavorito = documento.id
                        favoritos.add(restaurante)
                    }
                    result.invoke(UiState.Success(favoritos))
                    Log.d("FavoritoRepository", "Favoritos obtenidos con éxito: $favoritos")
                }
                .addOnFailureListener { exception ->
                    result.invoke(
                        UiState.Failure(
                            exception.localizedMessage ?: "Error desconocido al obtener Favoritos"
                        )
                    )
                    Log.e("FavoritoRepository", "Error al obtener Favoritos", exception)
                }
        } else {
            result.invoke(UiState.Failure("Usuario nulo, no se pueden obtener Favoritos"))
            Log.e("FavoritoRepository", "Usuario nulo, no se pueden obtener Favoritos")
        }
    }

    override fun addFavorito(
        restaurante: Restaurante,
        usuarioId: String,
        result: (UiState<Unit>) -> Unit
    ) {


        val document = database.collection(FireStoreCollection.FAVORITOS).document(restaurante.id.toString() + usuarioId)


        val favorito = hashMapOf(
            "id" to restaurante.id,
            "usuario" to usuarioId,
            "categoría" to restaurante.categoria,
            "contacto" to restaurante.contacto,
            "direccion" to restaurante.direccion,
            "horario" to restaurante.horario,
            "nombre" to restaurante.nombre,
            "imagen" to restaurante.imagen,
            "favorito" to restaurante.favorito
        )

        document.set(favorito)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Unit))
                Log.d("FavoritoRepository", "Restaurante añadido a favoritos con éxito")
            }
            .addOnFailureListener { exception ->
                result.invoke(
                    UiState.Failure(
                        exception.localizedMessage
                            ?: "Error desconocido al añadir restaurante a favoritos"
                    )
                )
                Log.e("FavoritoRepository", "Error al añadir restaurante a favoritos", exception)
            }


    }

    override fun eliminarFavorito(
        restaurante: Restaurante, userId: String
    ) {
         database.collection("favoritos").document(restaurante.id.toString()+ userId).delete() }

    }






