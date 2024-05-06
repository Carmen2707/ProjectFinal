package com.example.projectfinal.ui.favorito

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.data.repository.FavoritoRepository
import com.example.projectfinal.data.repository.ReservaRepository
import com.example.projectfinal.room.DAO
import com.example.projectfinal.util.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavoritosViewModel @Inject constructor (val repository: FavoritoRepository, private val dao: DAO) : ViewModel() {
    val _listaFavoritos = MutableLiveData<UiState<List<Restaurante>>>()
    val listaFavoritos: LiveData<UiState<List<Restaurante>>>
        get() = _listaFavoritos
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    fun cargarFavoritos(usuario: Usuario?) {
        repository.cargarFavoritos(usuario) { result ->
            if (result is UiState.Success) {
                _listaFavoritos.value = result
            } else if (result is UiState.Failure) {
                // Manejar el estado de fallo si es necesario
                Log.e("ReservaViewModel", "Error al cargar reservas:")
            }


        }

    }
    fun addFavoritos(restaurante: Restaurante){
        viewModelScope.launch {
            repository.addFavorito(restaurante, userId) { result ->
                if (result is UiState.Success) {
                    // Manejar el éxito según sea necesario
                } else if (result is UiState.Failure) {
                    // Manejar el error si es necesario
                    Log.e("RestauranteViewModel", "Error al agregar restaurante a favoritos")
                }
            }
        }
    }
    fun actualizarFavorito(restaurante: Restaurante, isChecked: Boolean) {
        viewModelScope.launch {
            restaurante.favorito = isChecked
            restaurante.userId = userId
            dao.actualizarRestaurante(restaurante)
           // _restaurantesBD.value = dao.getAll()
            addFavoritos(restaurante)

        }
    }
}