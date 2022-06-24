package com.examples.moviesfeed.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.examples.moviesfeed.ui.home.HomeActivity
import com.examples.moviesfeed.utils.SPLASH_DISPLAY_LENGTH
import dagger.hilt.android.AndroidEntryPoint

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.examples.moviesfeed.R.layout.activity_splash)
        Handler().postDelayed({
            val i = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(i)
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}