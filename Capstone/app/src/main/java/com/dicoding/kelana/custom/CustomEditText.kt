package com.dicoding.kelana.custom

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.kelana.R

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        context.theme.obtainStyledAttributes(attrs,
            R.styleable.CustomEditText,
            0, 0).apply {
                try {
                    var inputType = getInt(R.styleable.CustomEditText_inputType, 0)
                    when (inputType) {
                        1 -> {
                            hint = context.getString(R.string.username)
                            inputType = android.text.InputType.TYPE_CLASS_TEXT
                        }
                        2 -> {
                            hint = context.getString(R.string.email)
                            android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS.also { inputType = it }
                        }
                        3 -> {
                            hint = context.getString(R.string.password)
                            (android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD).also { inputType = it }
                            transformationMethod = PasswordTransformationMethod.getInstance()
                        }
                    }
                }
                finally {
                    recycle()
                }
        }
    }


}