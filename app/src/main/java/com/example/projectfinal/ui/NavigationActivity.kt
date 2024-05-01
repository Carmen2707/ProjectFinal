package com.example.projectfinal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.projectfinal.R
import com.example.projectfinal.databinding.ActivityNavigationBinding
import com.example.projectfinal.ui.favorito.FavoritosFragment
import com.example.projectfinal.ui.formulario.FormularioFragment
import com.example.projectfinal.ui.reserva.MisReservasFragment
import com.example.projectfinal.ui.restaurante.RestaurantesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remplazarFragments(RestaurantesFragment())
// Find the NavController for the NavHostFragment
      //  val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
      //  val navController = navHostFragment.navController
      //  binding.navigationBar.setupWithNavController(navController)
        binding.navigationBar.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                // Ir a la activity de home
                R.id.navigation_home -> remplazarFragments(RestaurantesFragment())
                R.id.navigation_favoritos -> remplazarFragments(FavoritosFragment())
                R.id.navigation_mis_reservas -> remplazarFragments(MisReservasFragment())
                R.id.navigation_formulario -> remplazarFragments(FormularioFragment())
                // Agregar más casos según sea necesario para otras opciones del menú
                else -> {

                }
            }
            true
        }
    }

    private fun remplazarFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }
}