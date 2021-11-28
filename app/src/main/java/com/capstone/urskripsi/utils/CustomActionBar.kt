package com.capstone.urskripsi.utils

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.ActionBar

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