package com.example.projectfinal.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectfinal.R
import com.example.projectfinal.databinding.ActivityIniciarBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.installations.installations

class IniciarActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityIniciarBinding
    private val GOOGLE_SIGN_IN = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs=getSharedPreferences("app", MODE_PRIVATE)
        establecerValoresSiExisten()
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
                            guardarPreferencias(email,password)
                            val intent = Intent(this, NavigationActivity::class.java)
                            startActivity(intent)

                        } else {
                            alerta()
                        }
                    }
            }

        }

        binding.btnGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.token)).requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        binding.btnVolver.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
         try {


             val account = task.getResult(ApiException::class.java)

             if (account != null) {
                 val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                 FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                     if (it.isSuccessful) {
                         irPortada(account.email ?: "", ProviderType.GOOGLE)
                     } else {
                         alerta()
                     }
                 }
             }
         }catch (e:ApiException){
             alerta()
         }
        }
    }

    private fun establecerValoresSiExisten(){
        val email=prefs.getString("email","")
        val password=prefs.getString("password","")
        val recordar=prefs.getBoolean("recordar",false)
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            binding.etEmailEditText.setText(email)
            binding.etPasswordEditText.setText(password)
            binding.cbRecordar.isChecked=recordar
        }
    }

    private fun guardarPreferencias(email: String,password: String){
        val editor=prefs!!.edit()
        if (binding.cbRecordar!!.isChecked){
            editor.putString("email",email)
            editor.putString("password",password)
            editor.putBoolean("recordar",true)
            editor.apply()
        }else{
            editor.clear()
            editor.putBoolean("recordar",false)
            editor.apply()
        }
    }
/*    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email !=null && provider !=null) {
            s
        }
    }*/
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