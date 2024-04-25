package com.example.projectfinal.ui.favorito

import android.os.Bundle
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
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.google.firebase.auth.FirebaseAuth

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var favoritosAdapter: FavoritosAdapter // Define tu adaptador de favoritos aquí
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
        if (userId != null) {
            viewModel.getFavoritos(userId).observe(viewLifecycleOwner) { favoritos ->
                // Actualizar el RecyclerView con la lista de favoritos
                favoritosAdapter.favoritos = favoritos
                favoritosAdapter.notifyDataSetChanged()
            }
        }


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
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
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