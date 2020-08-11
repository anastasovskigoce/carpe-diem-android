package com.alanford.carpediem.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R

// This class contains the the customers favorite quotes
// Created by ganastasovski on 8/2/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
class FavoritesFragment : Fragment() {

    private lateinit var noFavoritesText: TextView
    private lateinit var favoriteQuotesListRecyclerView: RecyclerView
    private var favoriteQuotesAdapter = FavoriteQuotesListAdapter(emptyList())

    private lateinit var favoriteQuotesListViewModel: FavoriteQuotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteQuotesListViewModel = ViewModelProviders.of(this).get(FavoriteQuotesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.favorites_layout, container, false)
        favoriteQuotesListRecyclerView = view.findViewById<RecyclerView>(R.id.favorite_quotes_recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteQuotesAdapter
        }

        noFavoritesText = view.findViewById(R.id.no_favorites_text)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteQuotesListViewModel.favoriteQuotesLiveData.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNullOrEmpty()) {
                    noFavoritesText.visibility = View.VISIBLE
                    favoriteQuotesListRecyclerView.visibility = View.GONE
                } else {
                    noFavoritesText.visibility = View.GONE
                    favoriteQuotesListRecyclerView.visibility = View.VISIBLE
                    favoriteQuotesListRecyclerView.adapter = FavoriteQuotesListAdapter(it)
                }
            }
        )
    }
}