package com.alanford.carpediem.quotelist

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.quotelist.QuoteListState.*


/**
 * Fragment that will get the list of quotes from the API and show them in a list
 */
class QuoteListFragment : Fragment() {

    private lateinit var noQuotesByAuthorTextView: TextView
    private lateinit var errorTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var quoteListRecyclerView: RecyclerView
    private var quoteListAdapter: QuoteListAdapter = QuoteListAdapter(emptyList())

    private lateinit var quoteListViewModel: QuoteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

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
        noQuotesByAuthorTextView = view.findViewById(R.id.no_quote_found_text)

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
                    is NoQuotesFoundByAuthor -> handleNoQuotesByAuthor()
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_quote_toolbar_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)

        searchView.apply {
            setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        quoteListViewModel.searchQuotesByAuthor(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
        }

        closeButton.setOnClickListener {
            quoteListViewModel.fetchQuotes()
            //Collapse the action view
            searchView.onActionViewCollapsed();
            //Collapse the search widget
            searchItem.collapseActionView();
        }
    }

    private fun handleQuotesFetched(quotes: List<Quote>) {
        noQuotesByAuthorTextView.visibility = GONE
        progressBar.visibility = GONE
        errorTextView.visibility = GONE
        quoteListRecyclerView.visibility = VISIBLE

        quoteListAdapter = QuoteListAdapter(quotes)
        quoteListRecyclerView.adapter = quoteListAdapter
    }

    private fun handleLoading() {
        noQuotesByAuthorTextView.visibility = GONE
        progressBar.visibility = VISIBLE
        errorTextView.visibility = GONE
        quoteListRecyclerView.visibility = GONE

        hideKeyboard()
    }

    private fun handleError() {
        noQuotesByAuthorTextView.visibility = GONE
        progressBar.visibility = GONE
        errorTextView.visibility = VISIBLE
        quoteListRecyclerView.visibility = GONE
    }

    private fun handleNoQuotesByAuthor() {
        noQuotesByAuthorTextView.visibility = VISIBLE
        progressBar.visibility = GONE
        errorTextView.visibility = GONE
        quoteListRecyclerView.visibility = GONE
    }

    private fun hideKeyboard() {
        activity?.currentFocus?.let {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
