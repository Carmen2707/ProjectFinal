package com.example.projectfinal.ui.reserva

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.data.repository.ReservaRepository
import com.example.projectfinal.util.UiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReservaViewModel @Inject constructor(val repository: ReservaRepository) : ViewModel() {
    private val _reserva = MutableLiveData<UiState<List<Reserva>>>()
    val reserva: LiveData<UiState<List<Reserva>>> get() = _reserva
    private val db = Firebase.firestore
    fun cargarReservas(usuario: Usuario?) {
        repository.cargarReservas(usuario) { result ->
            if (result is UiState.Success) {
                _reserva.value = result
            } else if (result is UiState.Failure) {
                // Manejar el estado de fallo si es necesario
                Log.e("ReservaViewModel", "Error al cargar reservas:")
            }


        }
    }

    fun borrarReserva(position: Int, callback: (Boolean) -> Unit) {
        val uiState = reserva.value
        if (uiState is UiState.Success) {
            val reservas = uiState.data.toMutableList()
            if (position in reservas.indices) {
                val reservaAEliminar = reservas[position]
                db.collection("reservas").document(reservaAEliminar.id)
                    .delete()
                    .addOnSuccessListener {
                        // Operación de eliminación exitosa
                        reservas.removeAt(position) // Eliminar el elemento de la lista local
                        _reserva.value =
                            UiState.Success(reservas) // Actualizar el estado con la lista modificada
                        callback(true)
                    }
                    .addOnFailureListener { exception ->
                        // Error al eliminar la reserva
                        Log.e("ReservaViewModel", "Error al eliminar reserva", exception)
                        callback(false)
                    }
            } else {
                // La posición está fuera de los límites de la lista de reservas
                callback(false)
            }
        } else {
            // El UiState actual no es un estado de éxito
            callback(false)
        }
    }


    fun addReserva(reserva: Reserva) {
        repository.addReserva(reserva)
    }

    fun actualizarReserva(reserva: Reserva) {
        repository.updateReserva(reserva)
    }
}