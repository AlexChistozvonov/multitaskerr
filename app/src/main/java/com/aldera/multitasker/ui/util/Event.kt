package com.aldera.multitasker.ui.util

class Event<T>(private val content: T?) {

    companion object {
        fun <T> empty() = Event<T>(null)
    }

    private var handled = false

    fun content() = if (handled) {
        null
    } else {
        handled = true
        content
    }
}
