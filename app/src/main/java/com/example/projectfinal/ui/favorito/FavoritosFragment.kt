package com.example.projectfinal.ui.favorito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentFavoritosBinding
import com.example.projectfinal.ui.auth.UsuarioViewModel
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.example.projectfinal.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private var favoritosAdapter: FavoritosAdapter? = null
    private val viewModel: RestauranteViewModel by activityViewModels()
    private val viewModelUsuario: UsuarioViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritosBinding.bind(view)

        viewModelUsuario.getSession().observe(viewLifecycleOwner) { usuario ->
            viewModel.cargarFragmentFavoritos(usuario)
        }

        viewModel.listaFavoritos.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    // Actualizar el adaptador con la lista de reservas
                    favoritosAdapter =
                        FavoritosAdapter(uiState.data.toMutableList(),
                            onFavoritoChangeListener = { restaurante, isChecked ->
                                esChecked(restaurante, isChecked)
                            },
                            onItemSelected = { restaurante ->
                                onItemSelected(restaurante)
                            }
                        )
                    binding.rvFavoritos.adapter = favoritosAdapter
                    favoritosAdapter?.updateList(uiState.data.toMutableList())
                }

                else -> {}
            }
        }
        binding.rvFavoritos.layoutManager = LinearLayoutManager(context)
    }

    private fun onItemSelected(restaurante: Restaurante) {
        val currentDestinationId = findNavController().currentDestination?.id
        val favoritosFragmentId = R.id.favoritosFragment

        if (currentDestinationId == favoritosFragmentId) {
            val action =
                FavoritosFragmentDirections.actionFavoritosFragmentToDetallesFragment(
                    restauranteNombre = restaurante.nombre, restaurante
                )
            findNavController().navigate(action)
        } else {
            Log.e("Navigation", "No se puede navegar desde el fragmento actual")

        }
    }

    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {
        viewModel.actualizarFavorito(restaurante, isChecked)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }
}