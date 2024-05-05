package com.example.projectfinal.ui.restaurante
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.example.projectfinal.ui.categoria.CategoriaAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


//TODO: GUARDAR RESTAURANTES EN ROOM
@AndroidEntryPoint
class RestaurantesFragment : Fragment() {
    private var email: String? = null
    private var nombre: String? = null

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var restauranteAdapter: RestauranteAdapter
    private var recyclerViewPosition = 0
    private val viewModel: RestauranteViewModel by activityViewModels()
    // Obtener el ID del usuario actual (puede variar según cómo manejes la autenticación)
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var categoriasSeleccionadas = mutableListOf<Categorias>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Guardar la posición actual del RecyclerView
        outState.putInt("recyclerViewPosition", (binding.rvRestaurantes.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition())
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
            viewModel.listaFiltrados.observe(viewLifecycleOwner) { restaurantes ->
                restauranteAdapter.submitList(restaurantes)
            }
            viewModel.filtrarRestaurantesPorCategoria(categoriasSeleccionadas)
        }

    }
    override fun onResume() {
        super.onResume()

        (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(recyclerViewPosition)


    }


    private fun onItemSelected(restaurante: Restaurante) {
       val action = RestaurantesFragmentDirections.actionRestaurantesFragmentToFormularioFragment(restauranteNombre = restaurante.nombre)
        val navController = findNavController()

        navController.navigate(action)

    }


    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {
        viewModel.actualizarFavorito(restaurante, isChecked)
      /*  recyclerViewPosition = restauranteAdapter.currentList.indexOf(restaurante)
        println(recyclerViewPosition)
        if (recyclerViewPosition != RecyclerView.NO_POSITION) {
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(recyclerViewPosition)
        }*/
    }






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantesBinding.bind(view)
        // Restaurar la posición del RecyclerView si está guardada
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getInt("recyclerViewPosition", 0)
        }

        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context)

        // Set the onFavoritoChangeListener


        // Observar los cambios en la lista de restaurantes desde el ViewModel
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
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(recyclerViewPosition)

        }


        // Configurar el adaptador de categorías
        categoriaAdapter =
            CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter

        savedInstanceState?.getInt("recyclerViewPosition")?.let { position ->
            recyclerViewPosition = position
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(position)
        }

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

}