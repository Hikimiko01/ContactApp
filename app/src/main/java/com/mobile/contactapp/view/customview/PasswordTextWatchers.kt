package com.mobile.contactapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mobile.contactapp.R

class PasswordTextWatchers(context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            s.let {
                if (s.toString().length < 8) {
                    error = context.getString(R.string.pass_error)
                } else {
                    error = null
                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

    fun setEditText(editText: TextInputEditText) {
        editText.addTextChangedListener(textWatcher)
    }

}