package com.example.projectfinal.ui.restaurante

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.ui.categoria.CategoriaAdapter
import com.example.projectfinal.ui.categoria.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint

//TODO: GUARDAR RESTAURANTES EN ROOM
@AndroidEntryPoint
class RestaurantesFragment : Fragment() {
    private var email: String? = null
    private var nombre: String? = null

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var restauranteAdapter: RestauranteAdapter

    private var listaOriginal = mutableListOf<Restaurante>()
    private var listaFiltrada = mutableListOf<Restaurante>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(email)
            nombre = it.getString(nombre)
        }


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
        Log.d("RestaurantesFragment", "Categoría seleccionada: ${categorias[position].javaClass.simpleName}, Estado: ${categorias[position].seleccionada}")
        categoriaAdapter.notifyItemChanged(position)
        filtrarRestaurantes()
    }


  /*  private fun onItemSelected(position: Int) {
        listaOriginal[position].seleccionada = !listaOriginal[position].seleccionada
        categoriaAdapter.notifyItemChanged(position)
        filtrarRestaurantes()
    }*/
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

      //  listaOriginal.clear()
        var nombre: String
        var direccion: String
        var horario: String
        var contacto: Long
        var imagen: String
        var categoria: String
        var seleccionada: Boolean
        var restaurante: Restaurante
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
                    restaurante = Restaurante(
                        nombre,
                        direccion,
                        horario,
                        contacto,
                        imagen,
                        categoria,
                        seleccionada
                    )

                listaOriginal.add(restaurante)

            }

          /*  binding.rvRestaurantes.adapter = RestauranteAdapter(listaOriginal){ position -> onItemSelected(position)}
            filtrarRestaurantes()*/
            restauranteAdapter.notifyDataSetChanged()
        }.addOnFailureListener { error ->
            Log.e("FirebaseError", error.message.toString())
        }

    }
    private fun filtrarRestaurantes() {
        listaFiltrada.clear()
        val selectedCategories = categorias.filter { it.seleccionada }
        if (selectedCategories.isEmpty()) {
            listaFiltrada.addAll(listaOriginal)
        } else {
            for (restaurante in listaOriginal) {
                if (selectedCategories.any { it.toString().contains(restaurante.categoria, ignoreCase = true) }) {
                    listaFiltrada.add(restaurante)
                }
            }
        }
        restauranteAdapter.restaurantes = listaFiltrada
        restauranteAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantesBinding.bind(view)

        // Configurar el adaptador de categorías
        categoriaAdapter =
            CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter

        // Configurar el RecyclerView de restaurantes
        restauranteAdapter = RestauranteAdapter(listaOriginal, )
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvRestaurantes.setHasFixedSize(true)
        binding.rvRestaurantes.adapter = restauranteAdapter

        // Obtener los datos de los restaurantes desde Firebase
        obtenerDatos()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }

    companion object {
        const val email = "email"
        const val nombre = "nombre"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RestaurantesFragment().apply {
                arguments = Bundle().apply {
                    putString(email, email)
                    putString(nombre, nombre)
                }
            }
    }
}