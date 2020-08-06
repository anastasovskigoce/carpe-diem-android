package com.alanford.carpediem.quotedetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.alanford.carpediem.R
import com.alanford.carpediem.extensions.setButtonFade

/**
 *  This Fragment will display the quote details
 */
class QuoteFragment : Fragment() {

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var shareImage: ImageView

    private lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val safeArgs: QuoteFragmentArgs by navArgs()
        val quote = safeArgs.quote

        quoteViewModel = ViewModelProviders.of(this).get(QuoteViewModel::class.java)
        quoteViewModel.loadQuote(quote)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = layoutInflater.inflate(R.layout.quote_detail, container, false)

        quoteTextView = view.findViewById(R.id.quote_text)
        authorTextView = view.findViewById(R.id.quote_author)
        shareImage = view.findViewById<ImageView>(R.id.share).apply {
            setOnClickListener {
                quoteViewModel.shareButtonClicked(getString(R.string.quote_detail_error_sharing))
            }
        }

        shareImage.setButtonFade()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //observe state
        quoteViewModel.quoteLiveData.observe(
            viewLifecycleOwner,
            Observer { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        is QuoteDetailsState.LoadQuote -> {
                            quoteTextView.text = it.quote.quoteText
                            authorTextView.text = it.quote.author
                        }
                    }
                }
            }
        )

        //observe event
        quoteViewModel.shareButtonClicked.observe(
            viewLifecycleOwner,
            Observer { event ->
                // Only proceed if the event has never been handled
                event.getContentIfNotHandled()?.let { quoteDetailsAction ->

                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, (quoteDetailsAction as QuoteDetailsAction.Share).quoteText)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        )
    }
}
