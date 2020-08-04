package com.alanford.carpediem.quotelist

import com.alanford.carpediem.data.Quote

// The state that the Quote List Fragment can have
// Created by ganastasovski on 8/3/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
sealed class QuoteListState {
    object LoadingState : QuoteListState()
    data class ListFetchedState(val quotes: List<Quote>) : QuoteListState()
    object ErrorState : QuoteListState()
}