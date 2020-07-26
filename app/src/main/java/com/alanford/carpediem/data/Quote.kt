package com.alanford.carpediem.data

// A data class that models a quote
// Created by ganastasovski on 7/21/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
data class Quote(
    private var id: String = "",
    var quoteText: String = "",
    var author: String = ""
)