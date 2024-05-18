package com.example.projectfinal.ui.reserva

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Restaurante
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
    private val _reservas = MutableLiveData<UiState<List<Reserva>>>()
    val reserva: LiveData<UiState<List<Reserva>>> get() = _reserva
    val reservasAdmin: LiveData<UiState<List<Reserva>>> get() = _reservas
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
            val reservas = uiState.data
            repository.borrarReserva(position, reservas) { success ->
                    if (success) {
                        val nuevasReservas = reservas.toMutableList()
                        nuevasReservas.removeAt(position)
                        _reserva.value = UiState.Success(nuevasReservas)
                    }
                    callback(success)
                }
            } else {
                callback(false)
            }
    }
    fun borrarReservaAdmin(position: Int, callback: (Boolean) -> Unit) {
        val uiState = reservasAdmin.value
        if (uiState is UiState.Success) {
            val reservas = uiState.data
            repository.borrarReserva(position, reservas) { success ->
                if (success) {
                    val nuevasReservas = reservas.toMutableList()
                    nuevasReservas.removeAt(position)
                    _reservas.value = UiState.Success(nuevasReservas)
                }
                callback(success)
            }
        } else {
            callback(false)
        }
    }

    fun addReserva(reserva: Reserva) {
        repository.addReserva(reserva)
    }

    fun actualizarReserva(reserva: Reserva) {
        repository.updateReserva(reserva)
    }

    fun cargarTodasReservasAdmin(restaurante: Restaurante?) {
        repository.cargarTodasReservasAdmin(restaurante) { result ->
            if (result is UiState.Success) {
                _reservas.value = result
            } else if (result is UiState.Failure) {
                Log.e("ReservaViewModel", "Error al cargar reservas:")
            }

        }
    }
}