package com.alanford.carpediem.quotedetails

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.alanford.carpediem.R
import com.alanford.carpediem.extensions.setButtonFade

/**
 *  This Fragment will display the quote details
 */
class QuoteDetailFragment : Fragment() {

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var shareImageView: ImageView
    private lateinit var favoriteImageView: ImageView

    private lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val safeArgs: QuoteDetailFragmentArgs by navArgs()
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

        shareImageView = view.findViewById<ImageView>(R.id.share).apply {
            setOnClickListener {
                quoteViewModel.shareButtonClicked(getString(R.string.quote_detail_error_sharing))
            }
        }

        favoriteImageView = view.findViewById<ImageView>(R.id.favorite).apply {
            setOnClickListener {
                quoteViewModel.favoritesButtonClicked()
            }
        }

        shareImageView.setButtonFade()
        favoriteImageView.setButtonFade()

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

        quoteViewModel.quoteFavorite.observe(
            viewLifecycleOwner,
            Observer {
                favoriteImageView.setImageResource(getFavoriteImageView(it != null))
            }
        )

        //observe event
        quoteViewModel.shareButtonClicked.observe(
            viewLifecycleOwner,
            Observer { event ->
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

        quoteViewModel.favoritesButtonClicked.observe(
            viewLifecycleOwner,
            Observer { event ->
                event.getContentIfNotHandled()?.let { quoteDetailsAction ->
                    when (quoteDetailsAction) {
                        is QuoteDetailsAction.FavoritesAdd -> {
                            favoriteImageView.setImageResource(R.drawable.ic_star_black_24dp)
                        }
                        is QuoteDetailsAction.FavoritesRemove -> {
                            favoriteImageView.setImageResource(R.drawable.ic_star_border_black_24dp)
                        }
                    }
                }
            }
        )
    }

    private fun getFavoriteImageView(isFavorite: Boolean) = if (isFavorite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp

}
