package com.example.projectfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.projectfinal.databinding.ActivityRegistrarBinding
import com.google.firebase.auth.FirebaseAuth


class RegistrarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            val emailEditText = binding.etEmail.editText // Obtiene la instancia de TextInputEditText
            val email = emailEditText?.text.toString() // Obtiene el texto del TextInputEditText
            val passwordEditText = binding.etPassword.editText // Obtiene la instancia de TextInputEditText
            val password = passwordEditText?.text.toString() // Obtiene el texto del TextInputEditText
            if (email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        irPortada(it.result?.user?.email ?: "",ProviderType.BASIC)
                    }else{
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

    private fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("error")
        builder.setMessage("se ha producido un error autenticando al usuario")
        builder.setPositiveButton("aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun irPortada(email:String, providerType: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", providerType.name)
            startActivity(intent)
        }
    }
}