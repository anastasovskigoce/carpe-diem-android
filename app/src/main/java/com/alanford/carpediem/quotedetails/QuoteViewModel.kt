package com.alanford.carpediem.quotedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.data.QuoteRepository

// This class will contain all the logic needed to show one particular quote
// Created by ganastasovski on 7/26/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteViewModel {
    private val quoteRepository = QuoteRepository.get()
    private val quoteIdLiveData = MutableLiveData<String>()

    var quoteLiveData: LiveData<Quote?> = Transformations.switchMap(quoteIdLiveData) { theQuoteId ->
        quoteRepository.getQuote(theQuoteId)
    }

    fun loadQuote(quoteId: String) {
        quoteIdLiveData.value = quoteId
    }
}