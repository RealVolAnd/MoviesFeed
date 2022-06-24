package com.examples.moviesfeed

import android.app.Application
import android.content.Context
import com.examples.moviesfeed.model.Movie
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
        var firstList: MutableList<Movie> = mutableListOf()
    }
}

val Context.app: App
    get() = applicationContext as App