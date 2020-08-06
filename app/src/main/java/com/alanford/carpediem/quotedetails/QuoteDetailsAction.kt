package com.alanford.carpediem.quotedetails

// Actions that we can do in the quote details fragment, such as bbutton clicks
// Created by ganastasovski on 8/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class QuoteDetailsAction {
    /**
     * TODO update this text here
     */
    data class Share(val quoteText: String): QuoteDetailsAction()
}