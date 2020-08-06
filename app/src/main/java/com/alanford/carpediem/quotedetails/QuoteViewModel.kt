package com.alanford.carpediem.quotedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alanford.carpediem.Event
import com.alanford.carpediem.data.Quote

// This class will contain all the logic needed to show one particular quote
// Created by ganastasovski on 7/26/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteViewModel : ViewModel() {

    private val _quoteLiveData = MutableLiveData<Event<QuoteDetailsState>>()
    val quoteLiveData: LiveData<Event<QuoteDetailsState>>
        get() = _quoteLiveData

    private val _shareButtonClicked = MutableLiveData<Event<QuoteDetailsAction>>()
    val shareButtonClicked: LiveData<Event<QuoteDetailsAction>>
        get() = _shareButtonClicked

    fun loadQuote(quote: Quote) {
        _quoteLiveData.value = Event(QuoteDetailsState.LoadQuote(quote))
    }

    fun shareButtonClicked(errorText: String) {
        val quoteDetailsState = _quoteLiveData.value?.peekContent()
        var quoteAuthorText = errorText

        if (quoteDetailsState != null && quoteDetailsState is QuoteDetailsState.LoadQuote) {
            quoteAuthorText = "${quoteDetailsState.quote.quoteText}   --${quoteDetailsState.quote.author}"
        }

        _shareButtonClicked.value = Event(
            QuoteDetailsAction.Share(quoteAuthorText)
        )
    }
}