package com.alanford.carpediem.favorites

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.extensions.setButtonFade
import com.alanford.carpediem.repository.FavoriteQuotesRepository

// A ViewHolder for favorite quotes
// Created by ganastasovski on 8/8/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class FavoriteQuotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var quote: Quote

    private val quoteText: TextView = view.findViewById(R.id.quote_text)
    private val author: TextView = view.findViewById(R.id.quote_author)
    private val shareButton: ImageView = view.findViewById(R.id.share)
    private val favoriteButton: ImageView = view.findViewById(R.id.favorite)

    init {
        shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, quote.quoteText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(view.context, shareIntent, null)
        }

        favoriteButton.setOnClickListener {
            val repository = FavoriteQuotesRepository.get()
            if (quote.isFavorite) {
                repository.removeQuote(quote)
            } else {
                repository.addQuoteToFavorites(quote)
            }

            quote.isFavorite = !quote.isFavorite

            favoriteButton.setImageResource(getFavoriteImageView(quote.isFavorite))
        }

        favoriteButton.setButtonFade()
        shareButton.setButtonFade()
    }

    fun bind(quote: Quote) {
        this.quote = quote
        quoteText.text = this.quote.quoteText
        author.text = this.quote.author
        favoriteButton.setImageResource(getFavoriteImageView(this.quote.isFavorite))
    }

    private fun getFavoriteImageView(isFavorite: Boolean) = if (isFavorite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp
}