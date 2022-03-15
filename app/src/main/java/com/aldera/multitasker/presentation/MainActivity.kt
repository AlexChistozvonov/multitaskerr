package com.aldera.multitasker.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.aldera.multitasker.R
import com.aldera.multitasker.ui.extension.hide
import com.aldera.multitasker.ui.extension.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Multitasker)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.launchFragment,
                R.id.loginFragment,
                R.id.startScreen,
                R.id.registrationFragment,
                R.id.recoveryPasswordEmailFragment,
                R.id.recoveryPasswordCodeFragment,
                R.id.recoveryPasswordCreateFragment -> navView.hide()
                else -> navView.show()
            }
        }
    }
}
