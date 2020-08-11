package com.alanford.carpediem.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote

// Adapter that will provide a list fo favorite quotes
// Created by ganastasovski on 8/8/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class FavoriteQuotesListAdapter(var quotes: List<Quote>) : RecyclerView.Adapter<FavoriteQuotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteQuotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_quote_view_holder, parent, false)
        return FavoriteQuotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    override fun onBindViewHolder(holder: FavoriteQuotesViewHolder, position: Int) {
        holder.bind(quotes[position])
    }
}