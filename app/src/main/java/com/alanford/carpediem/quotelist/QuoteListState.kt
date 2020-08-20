package com.alanford.carpediem.quotelist

import com.alanford.carpediem.data.Quote

// The state that the Quote List Fragment can have
// Created by ganastasovski on 8/3/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class QuoteListState {
    /**
     * The loading state, we show a spinner, waiting for the quotes to come
     */
    object LoadingState : QuoteListState()

    /**
     * We received the list of quotes, we can show them on the screen
     */
    data class ListFetchedState(val quotes: List<Quote>) : QuoteListState()

    /**
     * An error occurred getting the quotes from the API, show the error screen
     */
    object ErrorState : QuoteListState()

    /**
     * The user searched for quotes by an author but the API did not find any quotes by that author
     */
    object NoQuotesFoundByAuthor : QuoteListState()
}