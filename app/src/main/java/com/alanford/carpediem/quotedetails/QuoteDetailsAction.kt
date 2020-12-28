package com.alanford.carpediem.quotedetails

// Actions that we can do in the quote details fragment, such as bbutton clicks
// Created by ganastasovski on 8/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class QuoteDetailsAction {
    /**
     * Share button clicked
     */
    data class Share(val quoteText: String) : QuoteDetailsAction()

    /**
     * Add to favorites button clicked
     */
    object FavoritesAdd : QuoteDetailsAction()

    /**
     * Add to favorites button clicked
     */
    object FavoritesRemove : QuoteDetailsAction()

    /**
     * Thumbs up button pressed
     */
    data class ThumbsUp(val id: String) : QuoteDetailsAction()

    /**
     * Thumbs down button pressed
     */
    data class ThumbsDown(val id: String) : QuoteDetailsAction()
}