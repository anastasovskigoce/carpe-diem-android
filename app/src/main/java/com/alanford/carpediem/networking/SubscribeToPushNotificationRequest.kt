package com.alanford.carpediem.networking

import java.util.*

// Subscribe to push notification
// Created by ganastasovski on 10/20/20.
// Copyright (c) 2020 alana=ford. All rights reserved.
//
data class SubscribeToPushNotificationRequest(
    val id: String,
    val frequency: Int,
    val timeOfDay: Int,
    val timezoneId: String = TimeZone.getDefault().id,
    val isSubscribe: Boolean = true
)
