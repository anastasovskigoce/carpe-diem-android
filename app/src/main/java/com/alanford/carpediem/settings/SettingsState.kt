package com.alanford.carpediem.settings

// The state of the settings screen
// Created by ganastasovski on 10/12/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class SettingsState {

    /**
     * Notifications have been turned off
     */
    object NotificationsOff : SettingsState()

    /**
     * Receive notifications daily in the morning
     */
    object DailyMorning : SettingsState()

    /**
     * Receive notifications daily in the evening
     */
    object DailyEvening : SettingsState()

    /**
     * Receive Notifications weekly in the morning
     */
    object WeeklyMorning : SettingsState()

    /**
     * Receive Notifications weekly in the evening
     */
    object WeeklyEvening : SettingsState()
}