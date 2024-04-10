package com.example.projectfinal.data.repository

import android.util.Log
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.example.projectfinal.util.FireStoreCollection
import com.example.projectfinal.util.FireStoreDocumentField
import com.google.firebase.firestore.Query

class ReservaRepositoyImp (val database: FirebaseFirestore,
                           val storageReference: StorageReference): ReservaRepository {
    override fun getReservas(usuario: Usuario?, result: (UiState<List<Reserva>>) -> Unit) {
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
                    result.invoke(UiState.Failure(exception.localizedMessage ?: "Error desconocido al obtener reservas"))
                    Log.e("ReservaRepository", "Error al obtener reservas", exception)
                }
        } else {
            result.invoke(UiState.Failure("Usuario nulo, no se pueden obtener reservas"))
            Log.e("ReservaRepository", "Usuario nulo, no se pueden obtener reservas")
        }
    }

    override fun addReserva(reserva: Reserva, result: (UiState<Pair<Reserva, String>>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun updateReserva(reserva: Reserva, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun deleteReserva(reserva: Reserva, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

}