package com.example.tabatatimer.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.tabatatimer.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    
    private val SPLASH_DELAY = 2000L // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition if on Android 12+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen()
        }
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        // Delay and then start the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
} 