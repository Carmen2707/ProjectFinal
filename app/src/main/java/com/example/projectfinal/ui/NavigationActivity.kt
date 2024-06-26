package com.example.projectfinal.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.projectfinal.R
import com.example.projectfinal.databinding.ActivityNavigationBinding
import com.example.projectfinal.ui.restaurante.RestaurantesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navigationBar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
        navController = navHostFragment.navController
        navView.setupWithNavController(navController)
        remplazarFragments(RestaurantesFragment())


        binding.navigationBar.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.navigation_home -> findNavController(R.id.frame_layout).navigate(R.id.restaurantesFragment)
                R.id.navigation_favoritos -> findNavController(R.id.frame_layout).navigate(R.id.favoritosFragment)
                R.id.navigation_mis_reservas -> findNavController(R.id.frame_layout).navigate(R.id.misReservasFragment)
                else -> {
                }
            }
            true
        }

        onBackPressedDispatcher.addCallback(this) {
            val currentDestinationId = navController.currentDestination?.id
            val homeFragmentId = R.id.restaurantesFragment
            val reservaFragmentId = R.id.misReservasFragment
            val favoritosFragmentId = R.id.favoritosFragment
            if (currentDestinationId == homeFragmentId || currentDestinationId == reservaFragmentId || currentDestinationId == favoritosFragmentId) {
                finishAffinity()
            } else {
                navController.navigateUp()
            }
        }
    }

    private fun remplazarFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }
}