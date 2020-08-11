package com.alanford.carpediem.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

// A data class that models a quote
// Created by ganastasovski on 7/21/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
@Parcelize
@Entity
data class Quote(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @SerializedName("quote") var quoteText: String = "",
    var author: String = "",
    var rating: Int = 1,
    var isFavorite: Boolean = false
) : Parcelable

