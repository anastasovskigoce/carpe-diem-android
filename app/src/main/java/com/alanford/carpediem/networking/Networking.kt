package com.alanford.carpediem.networking

import com.alanford.carpediem.MainActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


// TThis class will contain the networking
// Created by ganastasovski on 7/28/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class Networking private constructor() {

    companion object {
        private var INSTANCE: Networking? = null

        fun initialize() {
            if (this.INSTANCE == null) {
                INSTANCE = Networking()
            }
        }

        fun get(): Networking {
            return INSTANCE ?: throw IllegalStateException("Networking must be initialized")
        }
    }

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MainActivity.Constants.CARPE_DIEM_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val carpeDiemApi: CarpeDiemApi = retrofit.create(CarpeDiemApi::class.java)
}