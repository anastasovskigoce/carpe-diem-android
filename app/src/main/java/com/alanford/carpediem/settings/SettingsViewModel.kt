package com.alanford.carpediem.settings

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alanford.carpediem.Event
import com.alanford.carpediem.R
import com.alanford.carpediem.networking.Networking
import com.alanford.carpediem.networking.SubscribeToPushNotificationRequest
import com.alanford.carpediem.notifications.NotificationFrequency
import com.alanford.carpediem.notifications.NotificationFrequency.*
import com.alanford.carpediem.notifications.NotificationTime
import com.alanford.carpediem.notifications.NotificationTime.*
import com.alanford.carpediem.settings.SaveSideEffect.*
import com.alanford.carpediem.settings.SettingsState.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

// A ViewModel for the settings fragment
// Created by ganastasovski on 10/12/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class SettingsViewModel(private val app: Application) : AndroidViewModel(app) {

    companion object {
        const val TAG = "SettingsViewModel"
    }

    private val _settingsLiveData = MutableLiveData<SettingsState>()
    val settingsLiveData: LiveData<SettingsState>
        get() = _settingsLiveData

    private val _saveSideEffect = MutableLiveData<Event<SaveSideEffect>>()
    val saveSideEffect: LiveData<Event<SaveSideEffect>>
        get() = _saveSideEffect

    private lateinit var notificationToken: String
    private val network = Networking.get()

    fun loadInitialState() {
        var state: SettingsState = NotificationsOff
        val sharedPreferencesKey: String = app.getString(R.string.shared_preferences)
        app.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)?.let {
            val areNotificationsOn = it.getBoolean(app.getString(R.string.notification_on_off), false)
            if (areNotificationsOn) {
                val frequency = it.getInt(app.getString(R.string.frequency), DAILY.ordinal)
                val time = it.getInt(app.getString(R.string.time), MORNING.ordinal)
                state = if (frequency == DAILY.ordinal) {
                    if (time == MORNING.ordinal) DailyMorning else DailyEvening
                } else {
                    if (time == MORNING.ordinal) WeeklyMorning else WeeklyEvening
                }
            }
            notificationToken =
                it.getString(app.getString(R.string.notification), "ErrorReadingTokenFromSharedPreferences") ?: "ErrorReadingTokenFromSharedPreferences"
        }
        _settingsLiveData.value = state
    }

    fun saveButtonClicked() {
        when (_settingsLiveData.value) {
            is NotificationsOff -> {
                network.carpeDiemApi.unsubscribe(notificationToken).enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "onFailure: failed to unsubscribe")
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d(TAG, "onResponse: unsubscribed")
                        val sharedPreferencesKey: String = app.getString(R.string.shared_preferences)
                        val sharedPref = app.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean(app.getString(R.string.notification_on_off), false)
                            commit()
                        }
                        _saveSideEffect.value = Event(ChangesSaved)
                    }
                })
            }
            is DailyEvening -> sendSubscribeRequest(DAILY, EVENING)
            is DailyMorning -> sendSubscribeRequest(DAILY, MORNING)
            is WeeklyEvening -> sendSubscribeRequest(WEEKLY, EVENING)
            is WeeklyMorning -> sendSubscribeRequest(WEEKLY, MORNING)
        }
    }

    private fun sendSubscribeRequest(frequency: NotificationFrequency, time : NotificationTime) {
        network.carpeDiemApi.subscribe(
            SubscribeToPushNotificationRequest(
                notificationToken,
                frequency.ordinal,
                time.ordinal
            )
        ).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                updateSharedPreferences(frequency, time)
            }
        })
    }

    private fun updateSharedPreferences(frequency: NotificationFrequency, time : NotificationTime) {
        val sharedPreferencesKey: String = app.getString(R.string.shared_preferences)
        val sharedPref = app.getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(app.getString(R.string.frequency), frequency.ordinal)
            putInt(app.getString(R.string.time), time.ordinal)
            commit()
        }
        _saveSideEffect.value = Event(ChangesSaved)

    }

    fun notificationSwitchToggled(toggledOn: Boolean) {
        _settingsLiveData.value = if (toggledOn) DailyMorning else NotificationsOff
    }

    fun dailyButtonClicked() {
        when (_settingsLiveData.value) {
            is WeeklyMorning -> _settingsLiveData.value = DailyMorning
            is WeeklyEvening -> _settingsLiveData.value = DailyEvening
        }

    }

    fun weeklyButtonClicked() {
        when (_settingsLiveData.value) {
            is DailyMorning -> _settingsLiveData.value = WeeklyMorning
            is DailyEvening -> _settingsLiveData.value = WeeklyEvening
        }

    }

    fun morningButtonClicked() {
        when (_settingsLiveData.value) {
            is WeeklyEvening -> _settingsLiveData.value = WeeklyMorning
            is DailyEvening -> _settingsLiveData.value = DailyMorning
        }

    }

    fun eveningButtonClicked() {
        when (_settingsLiveData.value) {
            is WeeklyMorning -> _settingsLiveData.value = WeeklyEvening
            is DailyMorning -> _settingsLiveData.value = DailyEvening
        }
    }
}