package com.alanford.carpediem.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// This class should provide you with data, either from the DB or API
// Created by ganastasovski on 7/26/20.
// Copyright (c) 2020 alarm.com. All rights reserved.
//
class QuoteRepository private constructor() {

    companion object {
        private var INSTANCE: QuoteRepository? = null

        fun initialize() {
            if (this.INSTANCE == null) {
                INSTANCE = QuoteRepository()
            }
        }

        fun get(): QuoteRepository {
            return INSTANCE ?: throw IllegalStateException("Quote Repository must be initialized")
        }
    }

    private var quotes: MutableLiveData<List<Quote>>? = null

    /**
     * Returns a particular quote
     * @param quoteId the ID of the quote you want to return
     */
    fun getQuote(quoteId: String): LiveData<Quote?> {
        return MutableLiveData<Quote>().apply {
            this.value = quotes?.value?.find { it.id == quoteId }
        }
    }

    /**
     * Returns the list of top quotes
     */
    fun getQuotes(): LiveData<List<Quote>>? {
        if (quotes == null) {
            quotes = MutableLiveData<List<Quote>>().apply {
                this.value = mutableListOf<Quote>().apply {
                    for (counter in 0 until 100) {
                        this.add(
                            Quote(
                                id = counter.toString(),
                                quoteText = "Quote $counter",
                                author = "Author $counter"
                            )
                        )
                    }
                }
            }
        }
        return quotes
    }
}