package com.example.projectfinal.ui.admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectfinal.R
import com.example.projectfinal.data.model.Restaurante
import com.example.projectfinal.databinding.ActivityAdminBinding
import com.example.projectfinal.databinding.ActivityReservasBinding
import java.io.Serializable

class ReservasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservasBinding

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

        val restaurante: Restaurante? = intent.getSerializableExtra("restaurante") as? Restaurante
        if (restaurante != null) {
            binding.tvRestaurantes.text = restaurante.nombre
        }
    }
}