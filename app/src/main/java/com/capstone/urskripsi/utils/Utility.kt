package com.capstone.urskripsi.utils

import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar

object Utility {

    // custom ActionBar to set Title Color
    fun ActionBar.setTitleColor(color: Int) {
        val text = SpannableString(title ?: "")
        text.setSpan(
            ForegroundColorSpan(color),
            0,
            text.length,
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        title = text
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
}