package com.alanford.carpediem.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.alanford.carpediem.data.Quote
import java.util.concurrent.Executors

// Repository for favorite quotes
// Created by ganastasovski on 8/7/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class FavoriteQuotesRepository private constructor(context: Context) {

    private val database: FavoriteQuotesDatabase = Room.databaseBuilder(
        context.applicationContext,
        FavoriteQuotesDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    private val executor = Executors.newSingleThreadExecutor()

    private val quoteDao = database.quoteDao()

    fun getQuotes(): LiveData<List<Quote>> = quoteDao.getQuotes()

    fun getQuote(uuid: String): LiveData<Quote?> = quoteDao.getQuote(uuid)

    fun removeQuote(quote: Quote) {
        executor.execute {
            quoteDao.removeQuote(quote)
        }
    }

    fun addQuoteToFavorites(quote: Quote) {
        executor.execute {
            quoteDao.addQuote(quote)
        }
    }

    companion object {
        private const val DATABASE_NAME = "quotes-database"

        private var INSTANCE: FavoriteQuotesRepository? = null

        fun initialize(context: Context) {
            if (this.INSTANCE == null) {
                INSTANCE = FavoriteQuotesRepository(context)
            }
        }

        fun get(): FavoriteQuotesRepository {
            return INSTANCE ?: throw IllegalStateException("FavoriteQuotesRepository must be initialized")
        }
    }

}