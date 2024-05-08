package com.example.projectfinal.data.repository

import android.util.Log
import com.example.projectfinal.data.model.Reserva
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
                    Log.e("ReservaRepository", "Error al obtener reservas", exception)
                }
        } else {
            result.invoke(UiState.Failure("Usuario nulo, no se pueden obtener reservas"))
            Log.e("ReservaRepository", "Usuario nulo, no se pueden obtener reservas")
        }
    }

    override fun addReserva(reserva: Reserva) {
        val document = database.collection("reservas").document()
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
        val document = database.collection("reservas").document(reserva.id)
        Log.e("id", reserva.id)
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
            database.collection("reservas").document(reservaAEliminar.id)
                .delete()
                .addOnSuccessListener {
                    // Operación de eliminación exitosa
                    val nuevasReservas = reservas.toMutableList()
                    nuevasReservas.removeAt(position) // Eliminar el elemento de la lista local
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    // Error al eliminar la reserva
                    Log.e("ReservaRepository", "Error al eliminar reserva", exception)
                    callback(false)
                }
        } else {
            // La posición está fuera de los límites de la lista de reservas
            callback(false)
        }
    }

}