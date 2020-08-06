package com.alanford.carpediem

// Used as a wrapper for data that is exposed via a LiveData that represents an event.
// Created by ganastasovski on 8/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}