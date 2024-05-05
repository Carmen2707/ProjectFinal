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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReservaViewModel @Inject constructor(val repository: ReservaRepository) : ViewModel() {
    private val _reserva = MutableLiveData<UiState<List<Reserva>>>()
    val reserva: LiveData<UiState<List<Reserva>>> get() = _reserva
    private val db = Firebase.firestore
    fun cargarReservas(usuario: Usuario?) {
        usuario?.let {
            _reserva.value = UiState.Loading
            repository.getReservas(usuario) { result ->
                _reserva.value = result
                Log.d("ReservaViewModel", "Reservas obtenidas con éxito: $result")
            }
        } ?: run {
            Log.e("ReservaViewModel", "No se puede obtener reservas, usuario nulo")
        }
    }

    fun borrarReserva() {
        db.collection("reservas").document(FirebaseAuth.getInstance().currentUser?.email ?: "")
            .delete()
    }

    fun addReserva(reserva: Reserva) {
        repository.addReserva(reserva)
    }
}