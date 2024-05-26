package com.example.projectfinal.data.repository

import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.util.FireStoreCollection
import com.example.projectfinal.util.FireStoreDocumentField
import com.google.firebase.firestore.FirebaseFirestore

class RestauranteRepositoryImp(private val database: FirebaseFirestore) : RestauranteRepository {
    override fun crearRestaurante(restaurante: Restaurante) {
        val document = database.collection("restaurantes")
            .document(restaurante.nombre)

        val restauranteNuevo = hashMapOf(
            "id" to restaurante.id,
            "nombre" to restaurante.nombre,
            "direccion" to restaurante.direccion,
            "horario" to restaurante.horario,
            "horaApertura" to restaurante.horaApertura,
            "horaCierre" to restaurante.horaCierre,
            "contacto" to restaurante.contacto,
            "imagen" to restaurante.imagen,
            "categoria" to restaurante.categoria,
            "carousel" to restaurante.imagenes,
            "web" to restaurante.web
        )
        document.set(restauranteNuevo)

    }

    override fun borrarRestaurante(
        position: Int,
        restaurantes: List<Restaurante>,
        callback: (Boolean) -> Unit
    ) {
        if (position in restaurantes.indices) {
            val restauranteAEliminar = restaurantes[position]
            val batch = database.batch()
            val restauranteDoc = database.collection(FireStoreCollection.RESTAURANTES)
                .document(restauranteAEliminar.nombre)
            batch.delete(restauranteDoc)
            // Eliminar favoritos relacionados
            database.collection(FireStoreCollection.FAVORITOS)
                .whereEqualTo("id", restauranteAEliminar.id)
                .get()
                .addOnSuccessListener { favoritosSnapshot ->
                    for (favorito in favoritosSnapshot) {
                        batch.delete(favorito.reference)
                    }

                    // Eliminar reservas relacionadas
                    database.collection(FireStoreCollection.RESERVA)
                        .whereEqualTo(
                            FireStoreDocumentField.RESTAURANTE_ID,
                            restauranteAEliminar.nombre
                        )
                        .get()
                        .addOnSuccessListener { reservasSnapshot ->
                            for (reserva in reservasSnapshot) {
                                batch.delete(reserva.reference)
                            }
                            batch.commit().addOnSuccessListener {
                                val nuevosRestaurantes = restaurantes.toMutableList()
                                nuevosRestaurantes.removeAt(position)
                                callback(true)
                            }.addOnFailureListener {
                                callback(false)
                            }
                        }.addOnFailureListener {
                            callback(false)
                        }
                }.addOnFailureListener {
                    callback(false)
                }
        } else {
            callback(false)
        }
    }
}