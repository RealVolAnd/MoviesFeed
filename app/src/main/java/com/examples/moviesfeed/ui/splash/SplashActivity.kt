package com.examples.moviesfeed.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.examples.moviesfeed.App
import com.examples.moviesfeed.R
import com.examples.moviesfeed.databinding.ActivitySplashBinding
import com.examples.moviesfeed.model.Movie
import com.examples.moviesfeed.ui.home.HomeActivity
import com.examples.moviesfeed.utils.MsgUtils
import com.examples.moviesfeed.utils.OFF
import com.examples.moviesfeed.utils.ON
import com.examples.moviesfeed.utils.START_PAGE_OFSET
import com.examples.moviesfeed.viewmodels.AppState
import com.examples.moviesfeed.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    lateinit var vb: ActivitySplashBinding
    private val viewModel: MoviesViewModel by viewModels()
    private var isConnectionError = false

    @Inject
    lateinit var msgUtils: MsgUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(vb.root)

        viewModel.getLiveData().observe(this) { renderData(it) }
        lifecycle.addObserver(viewModel)
        sendRequest()
    }

    private fun renderData(appState: AppState) {

        when (appState) {
            is AppState.Success -> {
                val movieData = appState.movieData
                setProgressBarState(OFF)
                setData(movieData)
                startMainApp()
            }
            is AppState.Loading -> {
                setProgressBarState(ON)
            }
            is AppState.Error -> {

                if (!isConnectionError) {
                    msgUtils.showConnectionError(vb.root, ::closeApp)
                    isConnectionError = true
                }
                sendRequest()
            }
        }
    }

    private fun setData(movieData: List<Movie>) {
        App.firstList.addAll(movieData)
    }

    private fun sendRequest() {
        viewModel.getMovieList(START_PAGE_OFSET)
    }

    private fun closeApp() {
        finish()
    }

    private fun setProgressBarState(state: Boolean) {
        when (state) {
            OFF -> {
                val fade = Fade()
                fade.setDuration(1000)
                TransitionManager.beginDelayedTransition(vb.splashLoadingLayout, fade)
                vb.splashLoadingLayout.visibility = View.GONE
            }
            ON -> {
                vb.splashLoadingLayout.visibility = View.VISIBLE
            }
            else -> {}
        }

    }

    private fun startMainApp() {
        val i = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(i)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        closeApp()
    }

}