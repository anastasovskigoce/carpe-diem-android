package com.alanford.carpediem.quotelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.networking.Networking
import com.alanford.carpediem.quotelist.QuoteListState.*
import retrofit2.Call


/**
 * Fragment that will get the list of quotes from the API and show them in a list
 */
class QuoteListFragment : Fragment() {

    private lateinit var errorTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var quoteListRecyclerView: RecyclerView
    private var quoteListAdapter: QuoteListAdapter = QuoteListAdapter(emptyList())

    private lateinit var quoteListViewModel: QuoteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteListViewModel = ViewModelProviders.of(this).get(QuoteListViewModel::class.java)
        quoteListViewModel.fetchQuotes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.quote_list, container, false)

        quoteListRecyclerView = view.findViewById<RecyclerView>(R.id.quote_list).apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
            adapter = quoteListAdapter
        }

        progressBar = view.findViewById(R.id.progress_bar_quote_list)
        errorTextView = view.findViewById(R.id.error_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteListViewModel.quotesLiveData.observe(
            viewLifecycleOwner,
            Observer { state ->
                when (state) {
                    is ListFetchedState -> handleQuotesFetched(state.quotes)
                    is LoadingState -> handleLoading()
                    is ErrorState -> handleError()
                }
            }
        )
    }

    private fun handleQuotesFetched(quotes: List<Quote>) {
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE
        quoteListRecyclerView.visibility = View.VISIBLE

        quoteListAdapter = QuoteListAdapter(quotes)
        quoteListRecyclerView.adapter = quoteListAdapter
    }

    private fun handleLoading() {
        progressBar.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
        quoteListRecyclerView.visibility = View.GONE
    }

    private fun handleError() {
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
        quoteListRecyclerView.visibility = View.GONE
    }
}
