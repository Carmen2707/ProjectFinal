package com.example.projectfinal.ui.restaurante

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.ui.categoria.CategoriaAdapter
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

//TODO: GUARDAR RESTAURANTES EN ROOM
@AndroidEntryPoint
class RestaurantesFragment : Fragment() {
    private var email: String? = null
    private var nombre: String? = null

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var restauranteAdapter: RestauranteAdapter

    private var listaOriginal = MutableLiveData<List<Restaurante>>()
    private var listaFiltrada = mutableListOf<Restaurante>()
    private val viewModel: RestauranteViewModel by viewModels()
    // Obtener el ID del usuario actual (puede variar según cómo manejes la autenticación)
    val userId = FirebaseAuth.getInstance().currentUser?.uid

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

        // Filtrar los restaurantes nuevamente
        filtrarRestaurantes()


    }



    /*  private fun onItemSelected(position: Int) {
        listaOriginal[position].seleccionada = !listaOriginal[position].seleccionada
        categoriaAdapter.notifyItemChanged(position)
        filtrarRestaurantes()
    }*/
    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {
        restaurante.favorito = isChecked
        viewModel.actualizarFavorito(restaurante, isChecked)
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
        viewModel.obtenerDatos()
        listaOriginal = viewModel.restaurantesBD
        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context)
        // Observar los cambios en la lista de restaurantes desde el ViewModel
        viewModel.restaurantesBD.observe(viewLifecycleOwner) { restaurantes ->
            listaOriginal.clear()
            listaOriginal.addAll(restaurantes)
            restauranteAdapter = RestauranteAdapter(listaOriginal) { restaurante, isChecked ->
                esChecked(restaurante, isChecked)
            }
            binding.rvRestaurantes.adapter = restauranteAdapter
        }
        // Configurar el adaptador de categorías
        categoriaAdapter =
            CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter

/*
        // Dentro del método onViewCreated en RestaurantesFragment
        restauranteAdapter = RestauranteAdapter(listaOriginal) { restaurante, isChecked ->
            esChecked(restaurante, isChecked)
        }


        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context)
        binding.rvRestaurantes.adapter = restauranteAdapter*/



       /* if (userId != null) {
            viewModel.getFavoritos(userId).observe(viewLifecycleOwner) { favoritos ->
                listaOriginal.forEach { restaurante ->
                    // Marcar el checkbox si el restaurante está en la lista de favoritos
                    restaurante.isChecked = favoritos.any { it.restauranteId == restaurante.id }
                }
                restauranteAdapter.notifyDataSetChanged()
            }
        }*/

    }

    /*override fun onDestroyView() {
        super.onDestroyView()
        // Detener la observación del ViewModel cuando el fragmento se destruya
        viewModel.restaurantesBD.removeObservers(viewLifecycleOwner)
    }*/

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