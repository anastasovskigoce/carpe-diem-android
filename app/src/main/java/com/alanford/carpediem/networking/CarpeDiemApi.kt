package com.alanford.carpediem.networking

import com.alanford.carpediem.data.Quote
import retrofit2.Call
import retrofit2.http.GET

// Use this interface to interact with the Carpe Diem API
// Created by ganastasovski on 7/28/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
interface CarpeDiemApi {
    /**
     * Get the list of quotes
     */
    @GET("/top-quotes")
    fun fetchQuotes(): Call<List<Quote>>
}