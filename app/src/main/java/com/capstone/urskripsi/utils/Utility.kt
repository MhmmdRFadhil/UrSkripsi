package com.capstone.urskripsi.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

object Utility {

    // custom ActionBar to set Title Color
    fun AppCompatActivity.simpleToolbar(toolbarTitle: String, toolbar: Toolbar?, navigationIcon: Boolean) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = toolbarTitle
            setDisplayHomeAsUpEnabled(navigationIcon)
        }
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.getStringFromName(name: String): String {
        return getString(resources.getIdentifier(name, "string", packageName))
    }
}