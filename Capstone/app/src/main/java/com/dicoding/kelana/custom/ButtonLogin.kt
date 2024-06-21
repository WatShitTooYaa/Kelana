package com.dicoding.kelana.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.kelana.R

class ButtonLogin : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) :super(context, attrs) {
        initAttributes(context, attrs)
    }

    private var txtColor: Int = 0
    private var bgColor: Drawable
    private var state: String = "login"

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.black)
        bgColor = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ButtonLogin, 0, 0
        )

        try {
            state = typedArray.getString(R.styleable.ButtonLogin_state) ?: "login"
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = bgColor
        textSize = 12f
        gravity = Gravity.CENTER
        setTextColor(txtColor)
        text = when (state) {
            "login" ->  ContextCompat.getString(context, R.string.login)
            "register" -> ContextCompat.getString(context, R.string.register)
            "logout" -> ContextCompat.getString(context, R.string.logout)
            "google_login" -> ContextCompat.getString(context, R.string.google_login_text  )
            "google_register" -> ContextCompat.getString(context, R.string.google_register_text  )
            else -> ""
        }
    }
}