package com.example.projectfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectfinal.databinding.ActivityIniciarBinding
import com.example.projectfinal.databinding.ActivityRestaurantesBinding

class RestaurantesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantesBinding
    private lateinit var categoriaAdapter: CategoriaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoriaAdapter = CategoriaAdapter(categorias){position -> actualizarCategorias(position)}
        binding.rvCategorias.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategorias.adapter = categoriaAdapter

    }

    private val categorias =
        listOf(Categorias.Italiano, Categorias.Indio, Categorias.China,Categorias.Japonesa,Categorias.Mediterránea,Categorias.Tapas, Categorias.Mexicano,
            Categorias.Cafés,Categorias.Vegetariano)

    private fun actualizarCategorias(position:Int){
        categorias[position].seleccionada=!categorias[position].seleccionada
        categoriaAdapter.notifyItemChanged(position)
        actualizarRestaurantes()
    }

    private fun actualizarRestaurantes() {
        TODO("Not yet implemented")
    }
}