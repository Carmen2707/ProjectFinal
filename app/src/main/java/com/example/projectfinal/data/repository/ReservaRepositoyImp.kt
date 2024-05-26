package com.example.projectfinal.data.repository

import android.util.Log
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.FireStoreCollection
import com.example.projectfinal.util.FireStoreDocumentField
import com.example.projectfinal.util.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ReservaRepositoyImp(val database: FirebaseFirestore) : ReservaRepository {
    override fun cargarReservas(usuario: Usuario?, result: (UiState<List<Reserva>>) -> Unit) {
        if (usuario != null) {
            database.collection(FireStoreCollection.RESERVA)
                .whereEqualTo(FireStoreDocumentField.USER_ID, usuario.email)
                .orderBy(FireStoreDocumentField.DATE, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val reservas = arrayListOf<Reserva>()
                    for (document in querySnapshot) {
                        val reserva = document.toObject(Reserva::class.java)
                        reservas.add(reserva)
                    }
                    result.invoke(UiState.Success(reservas))
                    Log.d("ReservaRepository", "Reservas obtenidas con éxito: $reservas")
                }
                .addOnFailureListener { exception ->
                    result.invoke(
                        UiState.Failure(
                            exception.localizedMessage ?: "Error desconocido al obtener reservas"
                        )
                    )
                }
        } else {
            result.invoke(UiState.Failure("Usuario nulo, no se pueden obtener reservas"))
        }
    }

    override fun cargarTodasReservasAdmin(
        restaurante: Restaurante?,
        result: (UiState<List<Reserva>>) -> Unit
    ) {
        if (restaurante != null) {
            database.collection(FireStoreCollection.RESERVA)
                .whereEqualTo(FireStoreDocumentField.RESTAURANTE_ID, restaurante.nombre)
                .orderBy(FireStoreDocumentField.DATE, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val reservas = arrayListOf<Reserva>()
                    for (document in querySnapshot) {
                        val reserva = document.toObject(Reserva::class.java)
                        reservas.add(reserva)
                    }
                    result.invoke(UiState.Success(reservas))
                    Log.d("ReservaRepository", "Reservas obtenidas con éxito: $reservas")
                }
                .addOnFailureListener { exception ->
                    result.invoke(
                        UiState.Failure(
                            exception.localizedMessage ?: "Error desconocido al obtener reservas"
                        )
                    )
                }
        } else {
            result.invoke(UiState.Failure("Restaurante nulo, no se pueden obtener reservas"))
        }
    }

    override fun addReserva(reserva: Reserva) {
        val document = database.collection(FireStoreCollection.RESERVA).document()
        reserva.id = document.id
        document
            .set(reserva)
            .addOnSuccessListener {
                Log.i("FIRE", "datos insertados correctamente")
            }
            .addOnFailureListener { error ->
                Log.e("FirebaseError", error.message.toString())
            }
    }

    override fun updateReserva(reserva: Reserva) {
        val document = database.collection(FireStoreCollection.RESERVA).document(reserva.id)
        document
            .set(reserva)
            .addOnSuccessListener {
                Log.i("ReservaRepository", "Reserva actualizada correctamente")
            }
            .addOnFailureListener { exception ->
                Log.e("ReservaRepository", "Error al actualizar la reserva", exception)
            }
    }


    override fun borrarReserva(
        position: Int,
        reservas: List<Reserva>,
        callback: (Boolean) -> Unit
    ) {
        if (position in reservas.indices) {
            val reservaAEliminar = reservas[position]
            database.collection(FireStoreCollection.RESERVA).document(reservaAEliminar.id)
                .delete()
                .addOnSuccessListener {
                    val nuevasReservas = reservas.toMutableList()
                    nuevasReservas.removeAt(position)
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("ReservaRepository", "Error al eliminar reserva", exception)
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

}