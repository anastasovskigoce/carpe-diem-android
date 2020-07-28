package com.alanford.carpediem.quotelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote

// n adapter that will provide us with a list of quotes
// Created by ganastasovski on 7/23/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteListAdapter(var quotes: List<Quote>) : RecyclerView.Adapter<QuoteListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quote_list_view_holder, parent, false)
        return QuoteListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun onBindViewHolder(holder: QuoteListViewHolder, position: Int) {
        holder.bind(quotes[position])
    }
}