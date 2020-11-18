package com.alanford.carpediem.notifications

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.alanford.carpediem.R
import com.alanford.carpediem.networking.Networking
import com.alanford.carpediem.networking.SubscribeToPushNotificationRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.util.*

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
        val network = Networking.get()
        token?.let {
            val subscribeRequest = network.carpeDiemApi.subscribe(
                SubscribeToPushNotificationRequest(
                    it,
                    NotificationFrequency.DAILY.ordinal,
                    NotificationTime.MORNING.ordinal,
                    TimeZone.getDefault().id
                )
            )

            subscribeRequest.enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // not sure what to do in this case
                    Log.d(TAG, "onFailure: token not saved")
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d(TAG, "onResponse: token saved")
                    val sharedPref = application.getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE) ?: return
                    with(sharedPref.edit()) {
                        putString(getString(R.string.notification), token)
                        putBoolean(getString(R.string.notification_on_off), true)
                        putInt(getString(R.string.frequency), NotificationFrequency.DAILY.ordinal)
                        putInt(getString(R.string.time), NotificationTime.MORNING.ordinal)
                        commit()
                    }
                }
            })
        }
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