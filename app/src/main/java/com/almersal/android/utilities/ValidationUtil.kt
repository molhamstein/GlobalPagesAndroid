package com.almersal.android.utilities

import android.content.Context
import android.widget.TextView
import com.almersal.android.R

object ValidationUtil {
    fun validate(context: Context , vararg editTexts: TextView): Boolean {
        var flag = true
        for (editText in editTexts) {
            if (editText.text.isBlank()) {
                editText.error = context.getString(R.string.required_field)
                flag = false
            } else
                editText.error = null
        }

        return flag
    }
}