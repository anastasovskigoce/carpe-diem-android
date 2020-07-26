package com.alanford.carpediem.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// This class should provide you with data, either from the DB or API
// Created by ganastasovski on 7/26/20.
// Copyright (c) 2020 alarm.com. All rights reserved.
//
class QuoteRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE: QuoteRepository? = null

        fun initialize(context: Context) {
            if (this.INSTANCE == null) {
                INSTANCE = QuoteRepository(context)
            }
        }

        fun get(): QuoteRepository {
            return INSTANCE ?: throw IllegalStateException("Quote Repository must be initialized")
        }
    }

    private var quotes: MutableLiveData<List<Quote>>? = null

    /**
     * Returns the list of top quotes
     */
    fun getQuotes(): LiveData<List<Quote>>? {
        if (quotes == null) {
            quotes = MutableLiveData<List<Quote>>().apply {
                this.value = generateFakeData()
            }
        }
        return quotes
    }

    private fun generateFakeData(): List<Quote> {
        return mutableListOf<Quote>().apply {
            for (counter in 0 until 100) {
                this.add(
                    Quote(
                        quoteText = "Quote $counter",
                        author = "Author $counter"
                    )
                )
            }
        }
    }
}