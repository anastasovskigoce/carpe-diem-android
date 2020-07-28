package com.alanford.carpediem.quotelist

import android.view.View
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote

// A ViewHolder that is used to show a quote on the details screen
// Created by ganastasovski on 7/23/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class QuoteListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var quote: Quote

    private val quoteText: TextView = view.findViewById(R.id.quote_text)
    private val author: TextView = view.findViewById(R.id.quote_author)

    init {
        view.setOnClickListener {
            val action = QuoteListFragmentDirections.actionQuoteListFragmentToQuoteFragment(quote.id)
            it.findNavController().navigate(action)
        }
    }

    fun bind(quote: Quote) {
        this.quote = quote
        quoteText.text = this.quote.quoteText
        author.text = this.quote.author
    }
}