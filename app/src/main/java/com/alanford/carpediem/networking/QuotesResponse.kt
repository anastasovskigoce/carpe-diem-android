package com.alanford.carpediem.networking

import com.alanford.carpediem.data.Quote

// The response of calling the list of quotes
// Created by ganastasovski on 7/28/20.
// Copyright (c) 2020 alarm.com. All rights reserved.
//
class QuotesResponse {
    //@SerializedName("quotes")
    lateinit var quoteItems: List<Quote>
}