package com.example.projectfinal.ui.reserva

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectfinal.util.UiState
import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.data.repository.ReservaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ReservaViewModel @Inject constructor( val repository: ReservaRepository): ViewModel() {
    private val _reserva = MutableLiveData<UiState<List<Reserva>>>()
    val reserva: LiveData<UiState<List<Reserva>>> get() = _reserva

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

}