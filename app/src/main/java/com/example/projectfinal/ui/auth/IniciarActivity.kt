package com.example.projectfinal.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.R
import com.example.projectfinal.databinding.ActivityIniciarBinding
import com.example.projectfinal.ui.MainActivity
import com.example.projectfinal.ui.NavigationActivity
import com.example.projectfinal.ui.ProviderType
import com.example.projectfinal.ui.admin.AdminActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class IniciarActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityIniciarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = getSharedPreferences("app", MODE_PRIVATE)
        establecerValoresSiExisten()
        binding.btnIniciar.setOnClickListener {
            if (validate()) {
                val emailEditText = binding.etEmail.editText
                val email = emailEditText?.text.toString()
                val passwordEditText = binding.etPassword.editText
                val password = passwordEditText?.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (email == "admin@admin.com") {
                                    irPortada(it.result?.user?.email ?: "", ProviderType.BASIC)
                                    guardarPreferencias(email, password)
                                    val intent = Intent(this, AdminActivity::class.java)
                                    startActivity(intent)
                                    Log.e("admin", "hhaha")
                                } else {
                                    irPortada(it.result?.user?.email ?: "", ProviderType.BASIC)
                                    guardarPreferencias(email, password)
                                    val intent = Intent(this, NavigationActivity::class.java)
                                    startActivity(intent)
                                }
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

    private fun establecerValoresSiExisten() {
        val email = prefs.getString("email", "")
        val password = prefs.getString("password", "")
        val recordar = prefs.getBoolean("recordar", false)
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            binding.etEmailEditText.setText(email)
            binding.etPasswordEditText.setText(password)
            binding.cbRecordar.isChecked = recordar
        }
    }

    private fun guardarPreferencias(email: String, password: String) {
        val editor = prefs.edit()
        if (binding.cbRecordar.isChecked) {
            editor.putString("email", email)
            editor.putString("password", password)
            editor.putBoolean("recordar", true)
            editor.apply()
        } else {
            editor.clear()
            editor.putBoolean("recordar", false)
            editor.apply()
        }
    }

    fun validate(): Boolean {
        var isValid = true

        val email = binding.etEmailEditText.text.toString()
        if (TextUtils.isEmpty(email)) {
            toggleTextInputLayoutError(binding.etEmail, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.etEmail, null)
        }

        val contrasena = binding.etPasswordEditText.text.toString()
        if (TextUtils.isEmpty(contrasena)) {
            toggleTextInputLayoutError(binding.etPassword, "Campo obligatorio")
            isValid = false
        } else {
            toggleTextInputLayoutError(binding.etEmail, null)
        }

        return isValid
    }

    private fun toggleTextInputLayoutError(
        @NonNull textInputLayout: TextInputLayout,
        msg: String?
    ) {
        textInputLayout.error = msg
        textInputLayout.isErrorEnabled = msg != null
    }

    private fun alerta() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Ee ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun irPortada(email: String, providerType: ProviderType) {
        Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", providerType.name)
            startActivity(intent)
        }
    }
}