package com.alanford.carpediem.networking

import com.alanford.carpediem.data.Quote
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

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

    @GET("/quote-by-author")
    fun searchQuotes(@Query("authorName") query: String): Call<List<Quote>>

    @POST("/subscribe/")
    fun subscribe(@Body body: SubscribeToPushNotificationRequest): Call<ResponseBody>

    @PUT("/unsubscribe/")
    fun unsubscribe(
        @Query("id") id: String
    ): Call<ResponseBody>
}