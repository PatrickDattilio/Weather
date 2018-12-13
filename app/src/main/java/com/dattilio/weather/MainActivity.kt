package com.dattilio.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.dattilio.weather.forecast.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DelayedToolbarTitleChanger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(findNavController(R.id.fragment))
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

    override fun changeTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if(fragment is WeatherFragment){
            fragment.setDelayedToolbarTitleChanger(this)
        }
    }
}

interface DelayedToolbarTitleChanger {
    fun changeTitle(title: String)
}
