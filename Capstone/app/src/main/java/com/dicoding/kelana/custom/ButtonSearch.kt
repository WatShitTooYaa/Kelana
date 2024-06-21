package com.dicoding.kelana.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.webkit.WebSettings.TextSize
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.kelana.R
import java.time.format.TextStyle

class ButtonSearch : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

//    private var txtColor: Int = 0
    private var txtColor = ContextCompat.getColor(context, android.R.color.black)
    private var enabledBackground: Drawable = ContextCompat.getDrawable(context, R.drawable.search_button_enable) as Drawable
    private var disabledBackground: Drawable = ContextCompat.getDrawable(context, R.drawable.search_button_disable) as Drawable
    private var searchIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.baseline_search_24) as Drawable
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        gravity = Gravity.CENTER
        setCompoundDrawablesWithIntrinsicBounds(null, searchIcon, null, null)
        compoundDrawablePadding = 3
        text = "Search"
//        textSize = 10.0f
    }

}