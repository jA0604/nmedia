package ru.netology.nmedia

import android.content.Context
import android.renderscript.ScriptGroup
import android.view.View
import android.view.inputmethod.InputMethodManager

public fun numberToK(number: Int) =
        when {
            (number >= 0 && number < 1_000) -> number.toString()
            (number > 999 && number < 10_000) -> String.format("%.1fK", number / 1_000.0)
            (number > 9_999 && number < 1_000_000) -> String.format("%.0fK", number / 1_000.0)
            (number > 999_999 && number < 1_000_000_000) -> String.format("%.1fM", number / 1_000_000.0)
            else -> "∞"
        }


object AndroidUtils {
    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}