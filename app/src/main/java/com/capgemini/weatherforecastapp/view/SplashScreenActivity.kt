package com.capgemini.weatherforecastapp.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capgemini.weatherforecastapp.R
import gr.net.maroulis.library.EasySplashScreen

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = EasySplashScreen(this)
            .withFullScreen()
            .withTargetActivity(MainActivity::class.java)
            .withSplashTimeOut(1500)
            .withBackgroundColor(Color.parseColor("#B2EBF2"))
            .withAfterLogoText("Weather Forecast App")
            .withLogo(R.mipmap.ic_launcher_foreground)

        config.afterLogoTextView.setTextColor(Color.BLACK)

        val easySplashScreen = config.create()
        setContentView(easySplashScreen)
    }
}