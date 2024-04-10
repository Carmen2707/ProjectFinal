package com.example.projectfinal.data.repository

import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.UiState

interface ReservaRepository {

    fun getReservas(usuario: Usuario?, result: (UiState<List<Reserva>>) -> Unit)
    fun addReserva(reserva: Reserva, result: (UiState<Pair<Reserva, String>>) -> Unit)
    fun updateReserva(reserva: Reserva, result: (UiState<String>) -> Unit)
    fun deleteReserva(reserva: Reserva, result: (UiState<String>) -> Unit)
}