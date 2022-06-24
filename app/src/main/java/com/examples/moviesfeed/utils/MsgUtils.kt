package com.examples.moviesfeed.utils

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.examples.moviesfeed.App
import com.examples.moviesfeed.R
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MsgUtils @Inject constructor() {
    fun showConnectionError(view: View, action: () -> Unit) {
        val snackBar = Snackbar.make(
            view, R.string.server_connection_error,
            Snackbar.LENGTH_INDEFINITE
        )
        val snackBarView = snackBar.view
        val textView =
            snackBarView.findViewById(R.id.snackbar_text) as TextView
        textView.setMaxLines(2)
        snackBar.setAction(R.string.repeat_string) {
            action()
        }
        snackBar.show()
    }

    fun showToast(text: String) {
        Toast.makeText(App.instance, text, Toast.LENGTH_LONG).show()
    }
}