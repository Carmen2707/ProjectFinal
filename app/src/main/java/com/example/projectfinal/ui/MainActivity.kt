package com.example.projectfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projectfinal.IniciarActivity
import com.example.projectfinal.RegistrarActivity
import com.example.projectfinal.databinding.PruebaBinding


enum class ProviderType{
    BASIC
}
class MainActivity : AppCompatActivity() {
    private lateinit var binding: PruebaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PruebaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIniciar.setOnClickListener{
            val intent = Intent(this, IniciarActivity::class.java)
//evita que pasemos de nuevo a la activity login
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnRegistrar.setOnClickListener{
            val intent = Intent(this, RegistrarActivity::class.java)
//evita que pasemos de nuevo a la activity login
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}