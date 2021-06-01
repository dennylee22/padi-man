package com.bangkit.capstone.padiman.viewModel

open class Event<out T>(private val content: T) {

    private var HasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (HasBeenHandled) {
            null
        } else {
            HasBeenHandled = true
            content
        }
    }
}