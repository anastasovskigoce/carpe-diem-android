package com.alanford.carpediem.networking

import com.alanford.carpediem.data.Quote
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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

    /**
     * Search quotes by author name
     */
    @GET("/quote-by-author")
    fun searchQuotes(@Query("authorName") query: String): Call<List<Quote>>

    /**
     * Subscribes to push notifications
     */
    @POST("/subscribe/")
    fun subscribe(@Body body: SubscribeToPushNotificationRequest): Call<ResponseBody>

    /**
     * Unsubscribe to push notifications
     */
    @PUT("/unsubscribe/")
    fun unsubscribe(
        @Query("id") id: String
    ): Call<ResponseBody>

    /**
     * Upvotes a quote and increases the rating by 1 vote
     */
    @PUT("/up-vote-quote/")
    fun upVote(
        @Query("id") id: String
    ): Call<ResponseBody>

    /**
     * Down votes a quote and reduces the rating by 1 vote
     */
    @PUT("/down-vote-quote/")
    fun downVote(
        @Query("id") id: String
    ): Call<ResponseBody>


}