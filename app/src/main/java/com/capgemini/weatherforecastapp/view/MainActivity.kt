package com.capgemini.weatherforecastapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.capgemini.weatherforecastapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), LocationListener {

    lateinit var lManager : LocationManager

    var latt = 0.0
    var long = 0.0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val editor = pref.edit()

        if(isNetworkAvailable(this)){
            Toast.makeText(this, "Network is available!", Toast.LENGTH_LONG).show()
            editor.putBoolean("isNetworkAvailable", true)
            editor.commit()
        }
        else{
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_LONG).show()
            editor.putBoolean("isNetworkAvailable", false)
            editor.commit()
        }

        checkPermission()
        lManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providerList = lManager.getProviders(true)
        var providerName = ""
        if(providerList.isNotEmpty()) {
            if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                providerName = LocationManager.GPS_PROVIDER
            } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                providerName = LocationManager.NETWORK_PROVIDER
            } else {
                providerName = providerList[0]
            }

            if (isNetworkAvailable(this)){
                noNetImg.visibility = View.GONE
                tv.visibility = View.GONE
                val loc = lManager.getLastKnownLocation(providerName)
                if (loc != null) {
                    latt = loc.latitude
                    long = loc.longitude

                    val locFrag = LocationFragment.newInstance(latt, long)
                    supportFragmentManager.beginTransaction().replace(R.id.parentL, locFrag).commit()
                } else {
                    Toast.makeText(this, "No Location found", Toast.LENGTH_LONG).show()
                }
            }
            else{
                CoroutineScope(Dispatchers.Main).launch {
                    noNetImg.visibility = View.VISIBLE
                    tv.visibility = View.VISIBLE
                    tv.text = "OOPS!\nNo Internet Connection!"
                    delay(2000)

                    val locIntent = Intent(this@MainActivity, WeatherBottomNavigation::class.java)
                    val bundle = Bundle()
                    bundle.putDouble("sentLattitude", 0.0)
                    bundle.putDouble("sentLongitude", 0.0)
                    locIntent.putExtra("values", bundle)
                    startActivity(locIntent)
                }
            }
        }
        else{
            Toast.makeText(this, "Please enable Location", Toast.LENGTH_LONG).show()
        }

    }

    private fun checkPermission(){

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
        }
        else{
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show()
        }
        else{
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
                else ->     false
            }
        }
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    override fun onLocationChanged(location: Location) {
        latt = location.latitude
        long = location.longitude
    }

}