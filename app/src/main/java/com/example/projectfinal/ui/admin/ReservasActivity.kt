package com.example.projectfinal.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.projectfinal.databinding.ActivityReservasBinding
import com.example.projectfinal.ui.reserva.ReservaViewModel
import com.example.projectfinal.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReservasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservasBinding
    private lateinit var reservasAdminAdapter: ReservasAdminAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private val viewModelReserva: ReservaViewModel by viewModels()
    private lateinit var restaurante: Restaurante
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReservasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        restaurante = (intent.getSerializableExtra("restaurante") as? Restaurante)!!

        viewModelReserva.cargarTodasReservasAdmin(restaurante)
        viewModelReserva.reservasAdmin.observe(this) { uiState ->
            when (uiState) {
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        binding.tvNoReservas.visibility = View.VISIBLE
                        binding.rvReservas.visibility = View.GONE
                    } else {
                        reservasAdminAdapter.updateList(uiState.data.toMutableList())
                        binding.tvNoReservas.visibility = View.GONE
                        binding.rvReservas.visibility = View.VISIBLE
                        reservasAdminAdapter.notifyDataSetChanged()
                    }
                }

                is UiState.Failure -> {
                    binding.tvNoReservas.visibility = View.VISIBLE
                    binding.rvReservas.visibility = View.GONE
                }

                else -> {}
            }
        }
        reservasAdminAdapter = ReservasAdminAdapter(mutableListOf())
        layoutManager = LinearLayoutManager(this)
        binding.rvReservas.layoutManager = layoutManager
        binding.rvReservas.adapter = reservasAdminAdapter

        binding.tvRestaurantes.text = restaurante.nombre

        binding.btnVolver.setOnClickListener {
            finish()
        }


        reservasAdminAdapter.setOnItemClickListener(object :
            ReservasAdminAdapter.OnItemClickListener {
            override fun onEditarClick(position: Int) {
                val reserva = reservasAdminAdapter.getItemAtPosition(position)

                val intent =
                    Intent(this@ReservasActivity, FormularioAdminActivity::class.java).apply {
                        putExtra("reserva", reserva)
                    }
                startActivity(intent)
            }


            override fun onBorrarClick(position: Int) {

                Log.d("MiFragmento", "Botón Borrar clickeado en la posición: $position")
                val builder = AlertDialog.Builder(this@ReservasActivity)
                builder.setMessage("¿Seguro que quieres eliminar esta reserva?")
                builder.setPositiveButton("Eliminar") { dialog, _ ->
                    viewModelReserva.borrarReservaAdmin(position) { success ->
                        if (success) {
                            reservasAdminAdapter.notifyItemRemoved(position)
                        }
                    }
                    dialog.dismiss()
                }
                builder.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setCancelable(false)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }

        })
    }

    private fun cargarTodasReservasAdmin() {
        viewModelReserva.cargarTodasReservasAdmin(restaurante)
    }

    override fun onResume() {
        super.onResume()
        // Asegurar que las reservas se recarguen al volver a esta actividad
        cargarTodasReservasAdmin()
    }
}