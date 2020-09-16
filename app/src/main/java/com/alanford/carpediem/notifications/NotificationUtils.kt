package com.alanford.carpediem.notifications

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alanford.carpediem.MainActivity
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.quotedetails.QuoteDetailFragmentArgs


// Extension functions for notifications
// Created by ganastasovski on 9/3/20.
// Copyright (c) 2020 alanford. All rights reserved.
//

// Notification ID.
private const val NOTIFICATION_ID = 0
const val QUOTE_TEXT = "QUOTE_TEXT"
const val AUTHOR = "AUTHOR"
const val QUOTE_ID = "QUOTE_ID"

/**
 * Builds and delivers the notification.
 */
fun NotificationManager.sendNotification(
    id: String,
    quoteText: String,
    author: String,
    title: String,
    messageBody: String,
    applicationContext: Context
) {

    val contentPendingIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.quoteDetailFragment)
        .setArguments(
            QuoteDetailFragmentArgs(
                Quote(
                    id = id,
                    quoteText = quoteText,
                    author = author
                )
            ).toBundle()
        )
        .createPendingIntent()

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.quote_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(NotificationCompat.InboxStyle())

    notify(NOTIFICATION_ID, builder.build())
}