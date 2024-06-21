package com.dicoding.kelana.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kelana.R
class CustomDividerItem(context: Context, private val divider: Drawable) : RecyclerView.ItemDecoration() {

    init {
        divider.setTint(ContextCompat.getColor(context, R.color.black)) // Set divider color
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        Log.d("CustomDividerItemDecoration", "onDraw called")

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            Log.d("CustomDividerItemDecoration", "for () called")
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top + params.topMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }

        // Draw divider above the first item
        if (childCount > 0) {
            Log.d("CustomDividerItemDecoration", "childCount called")
            val firstChild = parent.getChildAt(0)
            val firstParams = firstChild.layoutParams as RecyclerView.LayoutParams
            val firstTop = firstChild.top - firstParams.topMargin - divider.intrinsicHeight
            val firstBottom = firstChild.top - firstParams.topMargin

            divider.setBounds(left, firstTop, right, firstBottom)
            divider.draw(canvas)
        }
    }
}