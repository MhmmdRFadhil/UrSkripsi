package com.capstone.urskripsi.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.R

class DividerItemDecorator(context: Context) : RecyclerView.ItemDecoration() {
    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.divider_item)
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop: Int = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + (drawable?.intrinsicHeight ?: 0)
            drawable?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            drawable?.draw(c)
        }
    }
}