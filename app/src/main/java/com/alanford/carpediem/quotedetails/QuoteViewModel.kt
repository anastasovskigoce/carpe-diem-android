package com.alanford.carpediem.quotedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.alanford.carpediem.Event
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.repository.FavoriteQuotesRepository

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

    private val _favoritesButtonClicked = MutableLiveData<Event<QuoteDetailsAction>>()
    val favoritesButtonClicked: LiveData<Event<QuoteDetailsAction>>
        get() = _favoritesButtonClicked

    private val favoriteQuotesRepository = FavoriteQuotesRepository.get()
    private val quoteIdLiveData = MutableLiveData<String>()

    var quoteFavorite: LiveData<Quote?> = Transformations.switchMap(quoteIdLiveData) {
        favoriteQuotesRepository.getQuote(it)
    }

    fun loadQuote(quote: Quote) {
        _quoteLiveData.value = Event(QuoteDetailsState.LoadQuote(quote))
        quoteIdLiveData.value = quote.id
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

    fun favoritesButtonClicked() {
        // currently it is a favorite so remove it from favorites
        if (quoteFavorite.value != null) {
            val quoteDetailsState = _quoteLiveData.value?.peekContent()
            if (quoteDetailsState != null && quoteDetailsState is QuoteDetailsState.LoadQuote) {
                favoriteQuotesRepository.removeQuote(quoteDetailsState.quote)
            }

            _favoritesButtonClicked.value = Event(QuoteDetailsAction.FavoritesRemove)
        }
        // currently it is not a favorite so add it to favorites
        else {
            val quoteDetailsState = _quoteLiveData.value?.peekContent()
            if (quoteDetailsState != null && quoteDetailsState is QuoteDetailsState.LoadQuote) {
                favoriteQuotesRepository.addQuoteToFavorites(quoteDetailsState.quote.copy(isFavorite = true))
            }

            _favoritesButtonClicked.value = Event(QuoteDetailsAction.FavoritesAdd)
        }

    }
}
