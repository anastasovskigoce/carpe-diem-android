package com.alanford.carpediem.quotelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    val errorGettingData = MutableLiveData<Boolean>()
    val quotesLiveData = MutableLiveData<List<Quote>>()

    fun fetchQuotes() {
        val quotesRequest: Call<List<Quote>> = network.carpeDiemApi.fetchQuotes()

        quotesRequest.enqueue(object : Callback<List<Quote>> {
            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                errorGettingData.value = true
                Log.e("Network-OnFailure", "Error fetching quotes")
            }

            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                val quotesResponse = response.body() ?: mutableListOf()
                quotesLiveData.value = quotesResponse
                Log.i("Network-OnResponse", "Successfully received list of quotes")
            }
        })
    }

}