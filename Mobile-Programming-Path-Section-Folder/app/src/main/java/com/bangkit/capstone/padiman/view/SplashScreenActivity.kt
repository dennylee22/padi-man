package com.bangkit.capstone.padiman.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bangkit.capstone.padiman.MainActivity
import com.bangkit.capstone.padiman.R

class SplashScreenActivity : AppCompatActivity() {
    private val splashtime : Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, splashtime)
    }
}