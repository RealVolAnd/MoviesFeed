package com.examples.moviesfeed.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.examples.moviesfeed.R
import com.examples.moviesfeed.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var vb: ActivityHomeBinding

    override fun onBackPressed() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(vb.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frgmCont, HomeFragment())
                .commitAllowingStateLoss()
        }

        val topAppBar = vb.topAppBar
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close -> {
                    finishAndRemoveTask()
                    true
                }
                else -> false
            }
        }
    }
}