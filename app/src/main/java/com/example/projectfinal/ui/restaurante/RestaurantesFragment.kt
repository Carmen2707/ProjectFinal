package com.example.projectfinal.ui.restaurante

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.example.projectfinal.ui.categoria.CategoriaAdapter
import com.example.projectfinal.ui.favorito.FavoritosViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantesFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var restauranteAdapter: RestauranteAdapter
    private var recyclerViewPosition = 0
    private val viewModel: RestauranteViewModel by activityViewModels()
    private val viewModelFavoritos: FavoritosViewModel by activityViewModels()


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardar la posición actual del RecyclerView
        outState.putInt(
            "recyclerViewPosition",
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        )
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
        Log.d(
            "RestaurantesFragment",

            "Categoría seleccionada: ${categorias[position].javaClass.simpleName}, Estado: ${categorias[position].seleccionada}"
        )
        categoriaAdapter.notifyItemChanged(position)

        val categoriasSeleccionadas = mutableListOf<Categorias>()
        for (categoria in categorias) {
            if (categoria.seleccionada) {
                categoriasSeleccionadas.add(categoria)
                Log.e("cateforioa", categoriasSeleccionadas.toString())
            }
        }

        if (categoriasSeleccionadas.isEmpty()) {
            viewModel.restaurantesBD.observe(viewLifecycleOwner) { restaurantes ->
                restauranteAdapter.submitList(restaurantes)
            }
        } else {
            viewModel.filtrarRestaurantesPorCategoria(categoriasSeleccionadas)
            viewModel.listaFiltrados.observe(viewLifecycleOwner) { restaurantes ->
                restauranteAdapter.submitList(restaurantes)

            }

        }

    }

    override fun onResume() {
        super.onResume()
        (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(
            recyclerViewPosition
        )
    }


    private fun onItemSelected(restaurante: Restaurante) {
        val currentDestinationId = findNavController().currentDestination?.id
        val restaurantesFragmentId = R.id.restaurantesFragment

        if (currentDestinationId == restaurantesFragmentId) {
            val action = RestaurantesFragmentDirections.actionRestaurantesFragmentToFormularioFragment(
                restauranteNombre = restaurante.nombre,
                id = "",
                nombreUsuario = "",
                fecha = "",
                hora = "",
                personas = 1,
                observaciones = "",
                isEdit = false
            )
            findNavController().navigate(action)
        } else {
            Log.e("Navigation", "No se puede navegar desde el fragmento actual")

        }
    }


    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {

        viewModelFavoritos.actualizarFavorito(restaurante, isChecked)

          recyclerViewPosition = restauranteAdapter.currentList.indexOf(restaurante)
          println(recyclerViewPosition)
          if (recyclerViewPosition != RecyclerView.NO_POSITION) {
              (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(recyclerViewPosition)
          }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantesBinding.bind(view)


        // Restaurar la posición del RecyclerView si está guardada
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getInt("recyclerViewPosition", 0)
        }

        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context)

        viewModel.restaurantesBD.observe(viewLifecycleOwner) { restaurantes ->
            restauranteAdapter = RestauranteAdapter(
                onFavoritoChangeListener = { restaurante, isChecked ->
                    esChecked(restaurante, isChecked)

                },
                onItemSelected = { restaurante ->
                    onItemSelected(restaurante)
                }
            )
            binding.rvRestaurantes.adapter = restauranteAdapter

            restauranteAdapter.submitList(restaurantes)

            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(
                recyclerViewPosition
            )

        }


        // Configurar el adaptador de categorías
        categoriaAdapter =
            CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter
        categoriaAdapter.deseleccionarTodas()
        savedInstanceState?.getInt("recyclerViewPosition")?.let { position ->
            recyclerViewPosition = position
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(position)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        return binding.root
    }

}