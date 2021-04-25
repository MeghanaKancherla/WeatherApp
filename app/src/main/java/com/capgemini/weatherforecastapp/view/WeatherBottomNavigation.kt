package com.capgemini.weatherforecastapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capgemini.weatherforecastapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_weather_bottom_navigation.*

val PREF_NAME = "values"

class WeatherBottomNavigation : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_bottom_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val values: Bundle = intent.extras?.get("values") as Bundle
        var latt = values.getDouble("sentLattitude", 0.0)
        var longg = values.getDouble("sentLongitude", 0.0)
        Toast.makeText(this, "Lat: $latt\nLong: $longg", Toast.LENGTH_LONG).show()
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putFloat("lat",latt.toFloat())
        editor.putFloat("long", longg.toFloat())
        editor.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        if(!pref.getBoolean("isNetworkAvailable", true)){
            val info = menu?.add("NO INTERNET")
            info?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
        return super.onCreateOptionsMenu(menu)
    }
}