package com.dattilio.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(findNavController(R.id.fragment))

    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

    fun changeTitle(title: String) {
        supportActionBar?.title = title
    }
}
