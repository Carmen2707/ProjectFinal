package com.example.projectfinal.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.databinding.ActivityRegistrarBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class RegistrarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrar.setOnClickListener {
            if (validate()) {
                val emailEditText = binding.etEmail.editText
                val email = emailEditText?.text.toString()
                val passwordEditText = binding.etPassword.editText
                val password = passwordEditText?.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
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

    fun validate(): Boolean {
        var isValid = true

        val nombre = binding.etTextNombre.text.toString()
        if (TextUtils.isEmpty(nombre)) {
            toggleTextInputLayoutError(binding.etNombre, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.etNombre, null)
        }

        val email = binding.etTextEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            toggleTextInputLayoutError(binding.etEmail, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.etEmail, null)
        }

        val contrasena = binding.etTextPassword.text.toString()
        if (TextUtils.isEmpty(contrasena)) {
            toggleTextInputLayoutError(binding.etPassword, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.etPassword, null)
        }

        return isValid
    }


    /**
     * Display/hides TextInputLayout error.
     *
     * @param msg the message, or null to hide
     */
    private fun toggleTextInputLayoutError(
        @NonNull textInputLayout: TextInputLayout,
        msg: String?
    ) {
        textInputLayout.error = msg
        textInputLayout.isErrorEnabled = msg != null
    }
}