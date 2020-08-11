package com.alanford.carpediem.favorites

import androidx.lifecycle.ViewModel
import com.alanford.carpediem.repository.FavoriteQuotesRepository

// This class is the view mdoel for the favorite quotes
// Created by ganastasovski on 8/7/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class FavoriteQuotesViewModel : ViewModel() {

    private val favoriteQuotesRepository = FavoriteQuotesRepository.get()

    val favoriteQuotesLiveData = favoriteQuotesRepository.getQuotes()
}