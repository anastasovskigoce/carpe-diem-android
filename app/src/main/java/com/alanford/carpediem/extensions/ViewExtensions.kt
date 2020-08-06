package com.alanford.carpediem.extensions

import android.view.MotionEvent
import android.view.View

// Class that will contain extensions functions for a view
// Created by ganastasovski on 8/5/20.
// Copyright (c) 2020 alanford. All rights reserved.
//

/**
 * Changes the alpha of an image that is ussualy used a as button
 * Simulates a click event by changing the alpha
 */
fun View.setButtonFade() {
    this.setOnTouchListener { v, event ->
        if (v.isClickable) {
            val currentAlpha = v.alpha
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> v.alpha = currentAlpha / 2f
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.alpha = currentAlpha * 2f
            }
        }
        false
    }
}
