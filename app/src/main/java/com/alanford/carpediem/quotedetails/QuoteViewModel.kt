package com.alanford.carpediem.quotedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alanford.carpediem.data.Quote

// This class will contain all the logic needed to show one particular quote
// Created by ganastasovski on 7/26/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteViewModel : ViewModel() {
    private val _quoteLiveData = MutableLiveData<Quote>()
    val quoteLiveData: MutableLiveData<Quote>
            get() = _quoteLiveData

    fun loadQuote(quote: Quote) {
        _quoteLiveData.value = quote
    }
}