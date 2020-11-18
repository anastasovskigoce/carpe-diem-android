package com.alanford.carpediem.settings

import android.os.Bundle
import android.view.*
import android.view.View.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alanford.carpediem.R


// Settings Fragment, where you can modify your settings
// Created by alanford on 8/2/20.
// Copyright (c) 2020 ganastasovski. All rights reserved.
//
class SettingsFragment : Fragment() {

    private lateinit var seekBarNotificationOnOff: SwitchCompat
    private lateinit var dailyFrameLayout: FrameLayout
    private lateinit var weeklyFrameLayout: FrameLayout
    private lateinit var morningFrameLayout: FrameLayout
    private lateinit var eveningFrameLayout: FrameLayout
    private lateinit var dailyImageCheck: ImageView
    private lateinit var weeklyImageCheck: ImageView
    private lateinit var morningImageCheck: ImageView
    private lateinit var eveningImageCheck: ImageView

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        settingsViewModel.loadInitialState()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.settings_layout, container, false)

        seekBarNotificationOnOff = view.findViewById<SwitchCompat>(R.id.notification_on_off_switch).apply {
            setOnCheckedChangeListener { _, isChecked -> settingsViewModel.notificationSwitchToggled(isChecked) }
        }
        dailyFrameLayout = view.findViewById<FrameLayout>(R.id.once_a_day).apply {
            setOnClickListener { settingsViewModel.dailyButtonClicked() }
        }
        weeklyFrameLayout = view.findViewById<FrameLayout>(R.id.once_a_week).apply {
            setOnClickListener { settingsViewModel.weeklyButtonClicked() }
        }
        morningFrameLayout = view.findViewById<FrameLayout>(R.id.morning).apply {
            setOnClickListener { settingsViewModel.morningButtonClicked() }
        }
        eveningFrameLayout = view.findViewById<FrameLayout>(R.id.evening).apply {
            setOnClickListener { settingsViewModel.eveningButtonClicked() }
        }

        dailyImageCheck = view.findViewById(R.id.once_a_day_check)
        weeklyImageCheck = view.findViewById(R.id.once_a_week_check)
        morningImageCheck = view.findViewById(R.id.morning_check)
        eveningImageCheck = view.findViewById(R.id.evening_check)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_toolbar_menu, menu)
        menu.findItem(R.id.menu_item_check).apply {
            setOnMenuItemClickListener {
                settingsViewModel.saveButtonClicked()
                true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.saveSideEffect.observe(
            viewLifecycleOwner,
            Observer { event ->
                event.peekContent().let {
                    when (it) {
                        is SaveSideEffect.ChangesSaved -> {
                            //todo change to a snack bar
                            Toast.makeText(context, getString(R.string.changes_saved), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )

        //observe state
        settingsViewModel.settingsLiveData.observe(
            viewLifecycleOwner,
            Observer { event ->
                when (event) {
                    is SettingsState.NotificationsOff -> {
                        disableImages()
                        seekBarNotificationOnOff.isChecked = false
                        dailyImageCheck.visibility = GONE
                        weeklyImageCheck.visibility = GONE
                        morningImageCheck.visibility = GONE
                        eveningImageCheck.visibility = GONE
                    }
                    is SettingsState.DailyEvening -> {
                        enableImages()
                        seekBarNotificationOnOff.isChecked = true
                        dailyImageCheck.visibility = VISIBLE
                        weeklyImageCheck.visibility = GONE
                        morningImageCheck.visibility = GONE
                        eveningImageCheck.visibility = VISIBLE
                    }
                    is SettingsState.DailyMorning -> {
                        enableImages()
                        seekBarNotificationOnOff.isChecked = true
                        dailyImageCheck.visibility = VISIBLE
                        weeklyImageCheck.visibility = GONE
                        morningImageCheck.visibility = VISIBLE
                        eveningImageCheck.visibility = GONE
                    }
                    is SettingsState.WeeklyEvening -> {
                        enableImages()
                        seekBarNotificationOnOff.isChecked = true
                        dailyImageCheck.visibility = GONE
                        weeklyImageCheck.visibility = VISIBLE
                        morningImageCheck.visibility = GONE
                        eveningImageCheck.visibility = VISIBLE
                    }
                    is SettingsState.WeeklyMorning -> {
                        enableImages()
                        seekBarNotificationOnOff.isChecked = true
                        dailyImageCheck.visibility = GONE
                        weeklyImageCheck.visibility = VISIBLE
                        morningImageCheck.visibility = VISIBLE
                        eveningImageCheck.visibility = GONE
                    }
                }
            }
        )
    }

    private fun disableImages() {
        dailyFrameLayout.isEnabled = false
        weeklyFrameLayout.isEnabled = false
        morningFrameLayout.isEnabled = false
        eveningFrameLayout.isEnabled = false
    }

    private fun enableImages() {
        dailyFrameLayout.isEnabled = true
        weeklyFrameLayout.isEnabled = true
        morningFrameLayout.isEnabled = true
        eveningFrameLayout.isEnabled = true
    }
}