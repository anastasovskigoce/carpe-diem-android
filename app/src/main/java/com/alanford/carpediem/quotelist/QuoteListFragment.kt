package com.alanford.carpediem.quotelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote


/**
 * Fragment that will get the list of quotes from the API and show them in a list
 */
class QuoteListFragment : Fragment() {

    private lateinit var quoteListRecyclerView: RecyclerView
    private var quoteListAdapter: QuoteListAdapter =
        QuoteListAdapter(emptyList())

    private val quoteListViewModel: QuoteListViewModel by lazy {
        QuoteListViewModel()
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quoteListViewModel.quotesLiveData?.observe(
            viewLifecycleOwner,
            Observer { quotes -> updateUI(quotes) }
        )
    }

    private fun updateUI(quotes: List<Quote>) {
        quoteListAdapter = QuoteListAdapter(quotes)
        quoteListRecyclerView.adapter = quoteListAdapter
    }
}
