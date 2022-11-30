package com.frenzin.invoice.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.frenzin.invoice.introslider.IntroSliderActivity

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val intent = Intent(this, IntroSliderActivity::class.java)
        startActivity(intent)
        finish()
    }


}