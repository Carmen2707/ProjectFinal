package com.example.projectfinal.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.ActivityAdminBinding
import com.example.projectfinal.ui.MainActivity
import com.example.projectfinal.ui.restaurante.RestauranteViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val viewModel: RestauranteViewModel by viewModels()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var searchList: ArrayList<Restaurante>
    private lateinit var dataList: ArrayList<Restaurante>
    private lateinit var nombreList: ArrayList<String>
    private lateinit var categoriaList: ArrayList<String>
    override fun onResume() {
        super.onResume()
        viewModel.obtenerDatos()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCerrarSesion.setOnClickListener {
            val builder = AlertDialog.Builder(this@AdminActivity)
            builder.setMessage("¿Seguro que quieres cerrar sesión?")
            builder.setPositiveButton("Si") { dialog, _ ->
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.btnIrRest.setOnClickListener {
            val intent = Intent(this, AnadirRestauranteActivity::class.java)
            startActivity(intent)
        }
        layoutManager = LinearLayoutManager(this)
        binding.rvRestaurantes.layoutManager = layoutManager
        adminAdapter = AdminAdapter(
            onItemSelected = { restaurante ->
                onItemSelected(restaurante)
            }
        )
        binding.rvRestaurantes.adapter = adminAdapter
        viewModel.restaurantesBD.observe(this) { restaurantes ->

            adminAdapter.submitList(restaurantes)
            getData(restaurantes)
            for (restaurante in restaurantes) {
                nombreList.add(restaurante.nombre)
                categoriaList.add(restaurante.categoria)
            }
        }

        searchList = arrayListOf()
        dataList = arrayListOf()
        nombreList = arrayListOf()
        categoriaList = arrayListOf()
        binding.buscador.clearFocus()
        binding.buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.buscador.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    dataList.forEach {
                        if (it.nombre.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            it.categoria.toLowerCase(Locale.getDefault()).contains(searchText)
                        ) {
                            searchList.add(it)
                        }
                    }

                } else {
                    searchList.clear()
                    searchList.addAll(dataList)
                }
                adminAdapter.submitList(searchList)
                adminAdapter.notifyDataSetChanged()
                return false
            }

        })


    }

    private fun getData(restaurantes: List<Restaurante>) {
        dataList.clear()
        dataList.addAll(restaurantes)
        searchList.addAll(dataList)
        adminAdapter.submitList(searchList)
    }

    private fun onItemSelected(restaurante: Restaurante) {
        val intent = Intent(this, ReservasActivity::class.java).apply {
            putExtra("restaurante", restaurante)
        }
        startActivity(intent)
    }
}