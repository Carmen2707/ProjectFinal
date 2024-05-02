package com.example.projectfinal.ui.restaurante

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.data.repository.RepositoryFavorito
import com.example.projectfinal.room.DAO
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestauranteViewModel @Inject constructor(
    private val dao: DAO,
    private val repository: RepositoryFavorito
) : ViewModel() {

    val _restaurantesBD = MutableLiveData<List<Restaurante>>()
    val restaurantesBD: LiveData<List<Restaurante>>
        get() = _restaurantesBD

    val _listaFavoritos = MutableLiveData<List<Restaurante>>()
    val listaFavoritos: LiveData<List<Restaurante>>
        get() = _listaFavoritos

    val _listaFiltrados = MutableLiveData<List<Restaurante>>()
    val listaFiltrados: LiveData<List<Restaurante>>
        get() = _listaFiltrados

   val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        obtenerDatos() // Llama a la función para obtener los datos una vez que se inicializa el ViewModel
    }
    fun filtrarRestaurantesPorCategoria(categoriasSeleccionadas: List<Categorias>) {
        val restaurantesFiltrados = mutableListOf<Restaurante>()

        for (restaurante in _restaurantesBD.value ?: emptyList()) {
            if (categoriasSeleccionadas.isEmpty() || categoriasSeleccionadas.any { it.toString() == restaurante.categoria }) {
                restaurantesFiltrados.add(restaurante)
            }
        }

        _listaFiltrados.value = restaurantesFiltrados
    }

    fun actualizarFavorito(restaurante: Restaurante, isChecked: Boolean) {
        viewModelScope.launch {
            restaurante.favorito = isChecked
            restaurante.userId = userId
            dao.actualizarRestaurante(restaurante)

                _listaFavoritos.value = dao.getFavoritos(userId)

        }
    }
     fun actualizarListaRestaurantes(favoritos: List<Restaurante>) {
        val listaRestaurantes = restaurantesBD.value.orEmpty().toMutableList()
        for (restaurante in listaRestaurantes) {
            restaurante.favorito = favoritos.any { it.id == restaurante.id }
        }
        _restaurantesBD.value = listaRestaurantes
    }
    // Método para obtener los datos de los restaurantes desde Firebase
     fun obtenerDatos() {
        /** bdref = FirebaseDatabase.getInstance().getReference("restaurantes")
        bdref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
        for (restauranteSnapshot in snapshot.children) {
        val restaurante = restauranteSnapshot.getValue(Restaurante::class.java)
        if (!lista.contains(restaurante)) {
        lista.add(restaurante!!)
        }
        }


        binding.rvRestaurantes.adapter = RestauranteAdapter(lista)
        } else {
        Log.d("RestaurantesActivity", "No se encontraron datos en la base de datos")
        }
        }


        override fun onCancelled(error: DatabaseError) {
        Log.e("RestaurantesActivity", "Error al obtener datos: ${error.message}")
        }
        })**/

        val listaRestaurantes = mutableListOf<Restaurante>()
        // Limpiar la lista de restaurantes locales
        listaRestaurantes.clear()
        var id: Long
        var nombre: String
        var direccion: String
        var horario: String
        var contacto: Long
        var imagen: String
        var categoria: String
        var favorito: Boolean
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
                favorito = document.data.get("favorito") as Boolean
                restaurante = Restaurante(id,
                    nombre,
                    direccion,
                    horario,
                    contacto,
                    imagen,
                    categoria,
                    favorito
                )
                listaRestaurantes.add(restaurante)
                dao.insertAll(listaRestaurantes)

            }
            viewModelScope.launch {
              //  dao.insertAll(listaRestaurantes)
                _restaurantesBD.postValue(dao.getAll())

                    // Filtra y obtén solo los favoritos para el usuario actual


                Log.e("kjdsoidh", _restaurantesBD.value.toString())
                Log.e("FirebaseError", userId)
                Log.e("FirebaseError", _listaFavoritos.value.toString())
            }
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

    }
}