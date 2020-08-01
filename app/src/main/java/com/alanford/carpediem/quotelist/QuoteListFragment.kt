package com.alanford.carpediem.quotelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import retrofit2.Call


/**
 * Fragment that will get the list of quotes from the API and show them in a list
 */
class QuoteListFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var quoteListRecyclerView: RecyclerView
    private var quoteListAdapter: QuoteListAdapter = QuoteListAdapter(emptyList())

    private lateinit var quoteListViewModel: QuoteListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quoteListViewModel = ViewModelProviders.of(this).get(QuoteListViewModel::class.java)
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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteListViewModel.quotesLiveData.observe(
            viewLifecycleOwner,
            Observer { quotes ->
                progressBar.visibility = View.GONE
                quoteListRecyclerView.visibility = View.VISIBLE

                quoteListAdapter = QuoteListAdapter(quotes)
                quoteListRecyclerView.adapter = quoteListAdapter
            }
        )

        quoteListViewModel.errorGettingData.observe(
            viewLifecycleOwner,
            Observer { isError ->
                if (isError) {
                    progressBar.visibility = View.GONE
                    quoteListRecyclerView.visibility = View.VISIBLE
                    Toast.makeText(context, "An error occurred getting the quotes", Toast.LENGTH_SHORT).show()
                }
            }
        )

        quoteListViewModel.fetchQuotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        quoteListViewModel.quotesLiveData.removeObservers(viewLifecycleOwner)
        quoteListViewModel.errorGettingData.removeObservers(viewLifecycleOwner)
    }
}
