package com.almersal.android.utilities

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager



class MainHelper {
    companion object {

        fun hideKeyboard(view: View) {
            hideKeyboard(view, view.getContext())
        }

        fun hideKeyboard(view: View, activity: Context) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun showKeyboard(view: View) {
            showKeyboard(view, view.getContext())
        }

        fun showKeyboard(view: View, activity: Context) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, 0)
        }

        fun showSoftKeyboard(view: View, context: Context) {
            if (view.requestFocus()) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }


    }
}