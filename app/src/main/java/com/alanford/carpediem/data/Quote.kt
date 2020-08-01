package com.alanford.carpediem.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

// A data class that models a quote
// Created by ganastasovski on 7/21/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
@Parcelize
data class Quote(
    var id: String = "-1",
    @SerializedName("quote") var quoteText: String = "",
    var author: String = ""
) : Parcelable