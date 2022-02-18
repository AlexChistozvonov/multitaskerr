package com.aldera.multitasker.ui.extension

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

inline fun <T : View> T.showIf(condition: (T) -> Boolean): T {
    if (condition(this)) {
        show()
    } else {
        hide()
    }
    return this
}

fun Fragment.toast(message: String?) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun View.show() {
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.hide() {
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}