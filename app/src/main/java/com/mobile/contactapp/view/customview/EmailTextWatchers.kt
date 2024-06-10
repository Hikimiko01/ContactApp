package com.mobile.contactapp.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.mobile.contactapp.R

class EmailTextWatchers(context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                val email = it.toString()
                if (!email.matches(Regex("^[a-zA-Z0-9._%+-]+@gmail\\.com$"))) {
                    error = context.getString(R.string.email_error)
                } else {
                    error = null
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun setEditText(editText: TextInputEditText) {
        editText.addTextChangedListener(textWatcher)
    }
}