package com.example.aplikasiagriculture

import android.content.Intent
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
                R.id.navigation_data -> {
                    startActivity(Intent(this, AnalisaData::class.java))
                    true
                }
                R.id.navigation_grafik -> {
                    startActivity(Intent(this, AnalisaGrafik::class.java))
                    true
                }
                else -> false
            }
        }
    }
}