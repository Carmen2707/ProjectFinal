package com.example.projectfinal.ui.restaurante

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.model.Usuario
import com.example.projectfinal.data.repository.FavoritoRepository
import com.example.projectfinal.data.repository.UsuarioRepository

import com.example.projectfinal.util.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestauranteViewModel @Inject constructor(
    val repository: FavoritoRepository,
    val repositoryUsuario: UsuarioRepository
) : ViewModel() {

    val _restaurantesBD = MutableLiveData<List<Restaurante>>()
    val restaurantesBD: LiveData<List<Restaurante>>
        get() = _restaurantesBD
    val _listaFiltrados = MutableLiveData<List<Restaurante>>()
    val listaFiltrados: LiveData<List<Restaurante>>
        get() = _listaFiltrados
    val _listaFavoritos = MutableLiveData<UiState<List<Restaurante>>>()
    val listaFavoritos: LiveData<UiState<List<Restaurante>>>
        get() = _listaFavoritos
    private var userId = FirebaseAuth.getInstance().currentUser?.email ?: ""


    init {
        obtenerDatos()
        cargarFavoritos()
    }

    fun isFavorito(restaurante: Restaurante): Boolean {
        val favoritosState = _listaFavoritos.value
        return if (favoritosState is UiState.Success) {
            val favoritos = favoritosState.data
            favoritos.any { it.id == restaurante.id }
        } else {
            false
        }
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            val usuario = repositoryUsuario.obtenerUsuario(userId)
            repository.cargarFavoritos(usuario) { result ->
                if (result is UiState.Success) {
                    _listaFavoritos.value = result
                } else if (result is UiState.Failure) {
                    // Manejar el estado de fallo si es necesario
                    Log.e("RestauranteViewModel", "Error al cargar favoritos:")
                }
            }
        }
    }

    fun cargarFragmentFavoritos(usuario: Usuario?) {
        repository.cargarFavoritos(usuario) { result ->
            if (result is UiState.Success) {
                _listaFavoritos.value = result
            } else if (result is UiState.Failure) {
                Log.e("ReservaViewModel", "Error al cargar reservas:")
            }

        }
    }

    fun addFavoritos(restaurante: Restaurante) {
        // dao.modificarFavorito(restaurante.nombre, true)
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
            if (isChecked) {
                addFavoritos(restaurante)

            } else {
                eliminarFavorito(restaurante)

            }

        }
    }

    fun eliminarFavorito(restaurante: Restaurante) {

        viewModelScope.launch {
            repository.eliminarFavorito(restaurante, userId)
            val currentList = when (val currentState = _listaFavoritos.value) {
                is UiState.Success -> {
                    val dataList = currentState.data
                    dataList.toMutableList().apply {
                        // Eliminar el restaurante de la lista si existe
                        removeAll { it.id == restaurante.id }
                    }
                }

                else -> listOf() // En cualquier otro caso, devuelve una lista vacía
            }

            _listaFavoritos.value = UiState.Success(currentList)
        }
    }


    fun filtrarRestaurantesPorCategoria(categoriasSeleccionadas: MutableList<Categorias>) {
        val restaurantesFiltrados = mutableListOf<Restaurante>()

        val listaRestaurantes = _restaurantesBD.value

        // Verificar si la lista de restaurantes no es nula y no está vacía
        if (listaRestaurantes != null && listaRestaurantes.isNotEmpty()) {
            // Iterar sobre cada restaurante en la lista de restaurantes
            for (restaurante in listaRestaurantes) {
                // Verificar si el restaurante tiene alguna de las categorías seleccionadas
                if (categoriasSeleccionadas.any { it.javaClass.simpleName == restaurante.categoria }) {
                    // Si se cumple la condición de filtrado, agregar el restaurante a la lista de restaurantes filtrados
                    restaurantesFiltrados.add(restaurante)
                }
            }
            Log.e("rest", restaurantesFiltrados.toString())
        }

        _listaFiltrados.postValue(restaurantesFiltrados)
        Log.e("ssj", _listaFiltrados.value.toString())
    }


    fun obtenerDatos() {
        val listaRestaurantes = mutableListOf<Restaurante>()
        var id: Long
        var nombre: String
        var direccion: String
        var horario: String
        var contacto: Long
        var imagen: String
        var categoria: String
        var horaApertura: String
        var horaCierre: String
        var imagenes: List<String>
        var web: String
        var restaurante: Restaurante

        val db = Firebase.firestore
        db.collection("restaurantes").get().addOnSuccessListener { result ->
            for (document in result) {
                id = document.data.get("id") as Long
                nombre = document.data.get("nombre") as String
                direccion = document.data.get("direccion") as String
                horario = document.data.get("horario") as String
                contacto = document.data.get("contacto") as Long
                imagen = document.data.get("imagen") as String
                categoria = document.data.get("categoria") as String
                horaApertura = document.data.get("horaApertura") as String
                horaCierre = document.data.get("horaCierre") as String
                imagenes = document.data.get("carousel") as List<String>
                web = document.data.get("web") as String
                restaurante = Restaurante(
                    id,
                    nombre,
                    direccion,
                    horario,
                    horaApertura,
                    horaCierre,
                    contacto,
                    imagen,
                    categoria,
                    imagenes,
                    web
                    )

                if (isFavorito(restaurante)) {
                    restaurante.favorito = true
                }
                listaRestaurantes.add(restaurante)


            }
            viewModelScope.launch {
                _restaurantesBD.postValue(listaRestaurantes)
            }
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

    }




}