package com.example.projectfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.databinding.ActivityRestaurantesBinding
import com.google.firebase.Firebase


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore

class RestaurantesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private var lista= mutableListOf<Restaurante>()
    private lateinit var bdref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Configurar el adaptador de categorías
        categoriaAdapter = CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter
        // Obtener los datos de los restaurantes desde Firebase
        obtenerDatos()
        // Configurar el RecyclerView de restaurantes
        binding.rvRestaurantes.adapter = RestauranteAdapter(lista)
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRestaurantes.setHasFixedSize(true)



        println(lista.toString())
    }

    // Lista de categorías
    private val categorias =
        listOf(
            Categorias.Italiano, Categorias.Indio, Categorias.China, Categorias.Japonesa,
            Categorias.Mediterránea, Categorias.Tapas, Categorias.Mexicano,
            Categorias.Cafés, Categorias.Vegetariano
        )

    // Método para actualizar categorías
    private fun actualizarCategorias(position: Int) {
        categorias[position].seleccionada = !categorias[position].seleccionada
        categoriaAdapter.notifyItemChanged(position)
    }

    // Método para obtener los datos de los restaurantes desde Firebase
    private fun obtenerDatos() {
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


           var nombre: String
           var direccion: String
           var horario: String
           var contacto: Long
           var imagen: String
           var categoria: String
           var seleccionada: Boolean
           var restaurante:Restaurante
           val db = Firebase.firestore
           db.collection("restaurantes").get().addOnSuccessListener { result ->
               for (document in result) {
                   nombre = document.data.get("nombre") as String
                   direccion = document.data.get("direccion") as String
                   horario = document.data.get("horario") as String
                   contacto = document.data.get("contacto") as Long
                   imagen = document.data.get("imagen") as String
                   categoria = document.data.get("categoria") as String
                   seleccionada = document.data.get("seleccionada") as Boolean
                   restaurante = Restaurante(nombre,direccion,horario,contacto,imagen,categoria,seleccionada)

                   lista.add(restaurante)
               }

               // Aquí la lista ya está completa, así que puedes imprimir su contenido
               println(lista.toString())
               // Una vez que la lista esté lista, configuras el adaptador del RecyclerView
               binding.rvRestaurantes.adapter = RestauranteAdapter(lista)
           }.addOnFailureListener { error ->
               Log.e("FirebaseError", error.message.toString())
           }

    }


}
