package com.alanford.carpediem.quotedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.quotelist.QuoteListViewModel

/**
 *  This Fragment will display the quote details
 */
class QuoteFragment : Fragment() {

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView

    private lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val safeArgs: QuoteFragmentArgs by navArgs()
        val quoteId = safeArgs.quote

        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel::class.java)
        quoteViewModel.loadQuote(quoteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = layoutInflater.inflate(R.layout.quote_detail, container, false)

        quoteTextView = view.findViewById(R.id.quote_text)
        authorTextView = view.findViewById(R.id.quote_author)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quoteViewModel.quoteLiveData.observe(
            viewLifecycleOwner,
            Observer { quote ->
                quote?.let {
                    quoteTextView.text = it.quoteText
                    authorTextView.text = it.author
                }
            }
        )
    }
}
