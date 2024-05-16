package com.example.projectfinal.ui.restaurante

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.ui.auth.UsuarioViewModel
import com.example.projectfinal.data.model.Categorias
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.example.projectfinal.ui.categoria.CategoriaAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantesFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var restauranteAdapter: RestauranteAdapter
    private var recyclerViewPosition = 0
    private val viewModel: RestauranteViewModel by activityViewModels()
    private val viewModelUsuario: UsuarioViewModel by activityViewModels()

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
            Categorias.Italiano, Categorias.Indio, Categorias.Chino, Categorias.Japones,
            Categorias.Mediterráneo, Categorias.Tapas, Categorias.Mexicano,
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
            val action =
                RestaurantesFragmentDirections.actionRestaurantesFragmentToDetallesFragment(
                    restauranteNombre = restaurante.nombre, restaurante
                )
            findNavController().navigate(action)
        } else {
            Log.e("Navigation", "No se puede navegar desde el fragmento actual")

        }

    }


    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {

        viewModel.actualizarFavorito(restaurante, isChecked)

        recyclerViewPosition = restauranteAdapter.currentList.indexOf(restaurante)
        println(recyclerViewPosition)
        if (recyclerViewPosition != RecyclerView.NO_POSITION) {
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(
                recyclerViewPosition
            )
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRestaurantesBinding.bind(view)

        binding.btnPerfil.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("¿Seguro que quieres cerrar sesión?")
            builder.setPositiveButton("Si") { dialog, _ ->
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_restaurantesFragment_to_mainActivity)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
        // Restaurar la posición del RecyclerView si está guardada
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getInt("recyclerViewPosition", 0)
        }

        binding.rvRestaurantes.layoutManager = LinearLayoutManager(context)

        viewModel.restaurantesBD.observe(viewLifecycleOwner) { restaurantes ->
            for (restaurante in restaurantes) {
                restaurante.favorito = viewModel.isFavorito(restaurante) // Método para verificar si el restaurante está en la lista de favoritos
                Log.e("fav", restaurante.favorito.toString())
            }
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
            viewModelUsuario.getSession().observe(viewLifecycleOwner) { usuario ->
                viewModel.cargarFragmentFavoritos(usuario)

            }
        }

        // Configurar el adaptador de categorías
        categoriaAdapter =
            CategoriaAdapter(categorias) { position -> actualizarCategorias(position) }
        binding.rvCategorias.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter
        categoriaAdapter.resetearCategorias()
        savedInstanceState?.getInt("recyclerViewPosition")?.let { position ->
            recyclerViewPosition = position
            (binding.rvRestaurantes.layoutManager as LinearLayoutManager).scrollToPosition(position)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)
        return binding.root
    }

}