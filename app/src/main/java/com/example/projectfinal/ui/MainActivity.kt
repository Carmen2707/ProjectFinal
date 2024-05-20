package com.example.projectfinal.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.databinding.PortadaBinding
import com.example.projectfinal.ui.auth.IniciarActivity
import com.example.projectfinal.ui.auth.RegistrarActivity
import dagger.hilt.android.AndroidEntryPoint


enum class ProviderType {
    BASIC
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: PortadaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PortadaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        binding.btnIniciar.setOnClickListener {
            val intent = Intent(this, IniciarActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }
    }
}
