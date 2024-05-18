package com.example.projectfinal.data.repository

import com.example.projectfinal.data.model.Reserva
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.util.UiState

interface ReservaRepository {
    fun cargarTodasReservasAdmin(
        restaurante: Restaurante?,
        result: (UiState<List<Reserva>>) -> Unit
    )

    fun addReserva(reserva: Reserva)
    fun updateReserva(reserva: Reserva)

    fun cargarReservas(usuario: Usuario?, result: (UiState<List<Reserva>>) -> Unit)

    fun borrarReserva(position: Int, reservas: List<Reserva>, callback: (Boolean) -> Unit)
}