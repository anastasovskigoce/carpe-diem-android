package com.alanford.carpediem.quotedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote

/**
 *  This Fragment will display the quote details
 */
class QuoteFragment : Fragment() {

    private lateinit var quote: Quote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quote = Quote(quoteText = "A journey of a thousand miles begins with one step", author = "Confucius")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.quote_detail, container, false)

        val quoteTextView  = view.findViewById<TextView>(R.id.quote_text)
        quoteTextView.apply {
            this.text = quote.quoteText
        }

        val authorTextView = view.findViewById<TextView>(R.id.quote_author)
        authorTextView.apply {
            this.text = quote.author
        }

        return view
    }
}
