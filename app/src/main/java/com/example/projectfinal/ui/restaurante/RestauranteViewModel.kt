package com.example.projectfinal.ui.restaurante

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val restaurantesBD: LiveData<List<Restaurante>> = dao.getAll()

    fun actualizarFavorito(restaurante: Restaurante, isChecked: Boolean) {
        viewModelScope.launch {
            restaurante.favorito = isChecked
            restaurante.userId = getCurrentUserId() // Establecer el ID del usuario actual en el restaurante
            dao.actualizarRestaurante(restaurante) // Actualizar el restaurante en la base de datos
        }
    }


    fun getCurrentUserId(): String {
        // Aquí debes implementar la lógica para obtener el ID del usuario actual
        // Por ejemplo, podrías usar FirebaseAuth para obtener el ID del usuario actual
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    // Método para obtener los favoritos del usuario actual
    fun getFavoritos(userId: String): LiveData<List<Restaurante>> {
        return dao.getFavoritos(userId)
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
                // Si el restaurante está marcado como favorito, agregarlo a la lista y a la base de datos local
                if (favorito) {
                     restaurante = Restaurante(
                        id,
                        nombre,
                        direccion,
                        horario,
                        contacto,
                        imagen,
                        categoria,
                        favorito
                    )
                    listaRestaurantes.add(restaurante)
                }
            }
            viewModelScope.launch {
                dao.insertAll(listaRestaurantes)
            }
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

    }
}