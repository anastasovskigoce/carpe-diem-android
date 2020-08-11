package com.alanford.carpediem.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alanford.carpediem.data.Quote
import java.util.*

// Data access object for quotes
// Created by ganastasovski on 8/7/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
@Dao
interface QuoteDao {
    @Query("SELECT * FROM quote")
    fun getQuotes(): LiveData<List<Quote>>

    @Query("SELECT * FROM quote WHERE id = :quoteId")
    fun getQuote(quoteId: String): LiveData<Quote?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuote(quote: Quote)

    @Delete(entity = Quote::class)
    fun removeQuote(quote: Quote)
}