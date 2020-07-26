package com.alanford.carpediem.quotedetails

import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.data.QuoteRepository

// ViewModel for the QuoteListFragment
// Created by ganastasovski on 7/23/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteListViewModel {

    private val quoteRepository = QuoteRepository.get()
    val quotesLiveData = quoteRepository.getQuotes()

}