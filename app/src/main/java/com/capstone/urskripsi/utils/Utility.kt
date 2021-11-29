package com.capstone.urskripsi.utils

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.ActionBar

object Utility {
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
}