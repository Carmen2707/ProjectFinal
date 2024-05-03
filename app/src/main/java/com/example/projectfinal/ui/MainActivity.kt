package com.example.projectfinal.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.R
import com.example.projectfinal.databinding.PruebaBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


enum class ProviderType {
    BASIC, GOOGLE
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: PruebaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PruebaBinding.inflate(layoutInflater)
        setContentView(binding.root)

       /* //Guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()*/
        val auth = FirebaseAuth.getInstance()
       Log.d("email", auth.currentUser?.email.toString() )
        binding.btnIniciar.setOnClickListener {
            val intent = Intent(this, IniciarActivity::class.java)
//evita que pasemos de nuevo a la activity login
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
//evita que pasemos de nuevo a la activity login
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}