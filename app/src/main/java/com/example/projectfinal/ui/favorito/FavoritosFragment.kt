package com.example.projectfinal.ui.favorito

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.FragmentFavoritosBinding
import com.example.projectfinal.databinding.FragmentRestaurantesBinding
import com.example.projectfinal.ui.restaurante.RestauranteAdapter
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.google.firebase.auth.FirebaseAuth

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentFavoritosBinding
    private var favoritosAdapter: FavoritosAdapter? = null // Define tu adaptador de favoritos aquí
    private var listaFavoritos = mutableListOf<Restaurante>()
    private val viewModel: RestauranteViewModel by viewModels()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritosBinding.bind(view)
        viewModel.listaFavoritos.observe(viewLifecycleOwner) { restaurantes ->
            favoritosAdapter = FavoritosAdapter(restaurantes)
            binding.rvFavoritos.adapter = favoritosAdapter
        }


        binding.rvFavoritos.layoutManager = LinearLayoutManager(context)
    }


    /* private fun cargarListaFavoritos() {
        viewModel.getFavoritos(viewModel.getCurrentUserId()).observe(viewLifecycleOwner) { favoritos ->
            listaFavoritos.clear()
            listaFavoritos.addAll(favoritos)
            favoritosAdapter.actualizarFavoritos(listaFavoritos)
        }

    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        // Retorna la vista inflada por el binding
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoritosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritosFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}