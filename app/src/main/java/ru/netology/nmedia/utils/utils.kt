package ru.netology.nmedia

import android.content.Context
import android.renderscript.ScriptGroup
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

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
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

object KeyCode {
    const val EDIT_POST = 1001
    const val ADD_POST = 1002
    const val KEY_EDIT_POST = "key_edit_post"

}

enum class Action {
    LIKE,
    SHARE,
    POST
}

object NotificationType {
    const val BASIC_ACTION = 2001
    const val ALARM = 2100
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
    val datePublished: String

)

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private var pending = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        require (!hasActiveObservers()) {
            error("Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner) {
            if (pending) {
                pending = false
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(t: T?) {
        pending = true
        super.setValue(t)
    }
}
