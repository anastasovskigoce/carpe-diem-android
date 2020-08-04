package com.alanford.carpediem.quotelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alanford.carpediem.data.Event
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.networking.Networking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel for the QuoteListFragment
// Created by ganastasovski on 7/23/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteListViewModel : ViewModel() {

    private val network = Networking.get()

    private val _quotesLiveData = MutableLiveData<Event<QuoteListState>>()
    val quotesLiveData: MutableLiveData<Event<QuoteListState>>
        get() = _quotesLiveData

    init {
        _quotesLiveData.value = Event(QuoteListState.LoadingState)
    }

    fun fetchQuotes() {
        val quotesRequest: Call<List<Quote>> = network.carpeDiemApi.fetchQuotes()

        quotesRequest.enqueue(object : Callback<List<Quote>> {
            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                _quotesLiveData.value = Event(QuoteListState.ErrorState(""))
            }

            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                val quotesResponse = response.body() ?: mutableListOf()
                _quotesLiveData.value = Event(QuoteListState.ListFetchedState(quotesResponse))
            }
        })
    }
}