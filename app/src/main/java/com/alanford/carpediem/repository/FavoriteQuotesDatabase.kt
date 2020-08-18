package com.alanford.carpediem.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alanford.carpediem.data.Quote

// The database will contain the list of the user's favorite quotes
// Created by ganastasovski on 8/7/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
@Database(entities = [Quote::class], version = 1)
abstract class FavoriteQuotesDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
}