package com.alanford.carpediem.notifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// Firebase messaging service for quotes
// Created by ganastasovski on 9/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.notification?.let { payload ->
            // Check if message contains a data payload.
            remoteMessage.data.let { data ->
                Log.d(TAG, "Message data payload: " + remoteMessage.data)
                sendNotification(
                    id = data[QUOTE_ID].toString(),
                    quoteText = data[QUOTE_TEXT].toString(),
                    author = data[AUTHOR].toString(),
                    title = payload.title.toString(),
                    messageBody = payload.body.toString()
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    /**
     * Persist token to third-party servers.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        id: String,
        quoteText: String,
        author: String,
        title: String,
        messageBody: String
    ) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(id, quoteText, author, title, messageBody, applicationContext)
    }

    companion object {
        private const val TAG = "MessagingService"
    }
}