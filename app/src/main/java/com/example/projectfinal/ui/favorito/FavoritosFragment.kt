package com.example.projectfinal.ui.favorito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.UsuarioViewModel
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentFavoritosBinding
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.example.projectfinal.util.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritosFragment : Fragment() {


    private lateinit var binding: FragmentFavoritosBinding
    private var favoritosAdapter: FavoritosAdapter? = null // Define tu adaptador de favoritos aquí
    private var listaFavoritos = mutableListOf<Restaurante>()
    private val viewModel: RestauranteViewModel by activityViewModels()
    private val viewModelUsuario: UsuarioViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritosBinding.bind(view)


        viewModelUsuario.getSession().observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                viewModel.cargarFavoritos(usuario)

            } ?: run {
                Log.d("MisReservasFragment", "Usuario no autenticado")
            }
        }

        viewModel.listaFavoritos.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {
                    // Actualizar el adaptador con la lista de reservas
                    favoritosAdapter = FavoritosAdapter(uiState.data.toMutableList()) { restaurante, isChecked ->
                        esChecked(restaurante, isChecked)
                    }
                    binding.rvFavoritos.adapter = favoritosAdapter
                    favoritosAdapter?.updateList(uiState.data.toMutableList())
                }


                else -> {}
            }
        }
      /*  viewModel.listaFavoritos.observe(viewLifecycleOwner) { restaurantes ->
            favoritosAdapter = FavoritosAdapter(restaurantes) { restaurante, isChecked ->
                esChecked(restaurante, isChecked)
            }
            binding.rvFavoritos.adapter = favoritosAdapter

        }*/

        binding.rvFavoritos.layoutManager = LinearLayoutManager(context)
    }

    private fun esChecked(restaurante: Restaurante, isChecked: Boolean) {
        viewModel.actualizarFavorito(restaurante, isChecked)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }
}