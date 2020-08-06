package com.alanford.carpediem.quotedetails

import com.alanford.carpediem.data.Quote

// The states that the QuoteDetails fragment can have
// Created by ganastasovski on 8/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class QuoteDetailsState {
    /**
     * We received a quote that we can show on the screen
     */
    data class LoadQuote(val quote: Quote) : QuoteDetailsState()
}