package com.example.aplikasiagriculture

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle Home menu click
                    true
                }
                R.id.navigation_dashboard -> {
                    // Handle Dashboard menu click
                    true
                }
                R.id.navigation_notifications -> {
                    // Handle Notifications menu click
                    true
                }
                else -> false
            }
        }
    }
}