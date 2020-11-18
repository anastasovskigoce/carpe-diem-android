package com.alanford.carpediem.settings

// Side effects on clicking save on the setttings screen
// Created by ganastasovski on 10/20/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class SaveSideEffect {
    /**
     * Shows a message saying changes have been saved
     */
    object ChangesSaved : SaveSideEffect()
}