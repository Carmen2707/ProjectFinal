package com.example.projectfinal.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.databinding.ActivityIniciarBinding
import com.google.firebase.auth.FirebaseAuth

class IniciarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnIniciar.setOnClickListener {
            val emailEditText = binding.etEmail.editText
            val email = emailEditText?.text.toString()
            val passwordEditText = binding.etPassword.editText
            val password = passwordEditText?.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            irPortada(it.result?.user?.email ?: "", ProviderType.BASIC)

                            val intent = Intent(this, NavigationActivity::class.java)
                            startActivity(intent)

                        } else {
                            alerta()
                        }
                    }
            }

        }

        binding.btnVolver.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun alerta() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("error")
        builder.setMessage("se ha producido un error autenticando al usuario")
        builder.setPositiveButton("aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun irPortada(email: String, providerType: ProviderType) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", providerType.name)
            startActivity(intent)
        }
    }
}