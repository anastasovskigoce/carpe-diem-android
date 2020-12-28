package com.alanford.carpediem.quotedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.alanford.carpediem.Event
import com.alanford.carpediem.R
import com.alanford.carpediem.extensions.setButtonFade
import com.alanford.carpediem.networking.Networking
import com.alanford.carpediem.settings.SaveSideEffect
import com.alanford.carpediem.settings.SettingsViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/**
 *  This Fragment will display the quote details
 */
class QuoteDetailFragment : Fragment() {

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var shareImageView: ImageView
    private lateinit var favoriteImageView: ImageView
    private lateinit var thumbsDownImageView: ImageView
    private lateinit var thumbsUpImageView: ImageView
    private lateinit var mainLayout: ConstraintLayout

    private lateinit var quoteViewModel: QuoteViewModel

    private val network = Networking.get()

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

        mainLayout = view.findViewById(R.id.main_layout)
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

        thumbsUpImageView = view.findViewById<ImageView>(R.id.thumbs_up).apply {
            setOnClickListener {
                quoteViewModel.thumbsUpButtonClicked()
            }
        }

        thumbsDownImageView = view.findViewById<ImageView>(R.id.thumbs_down).apply {
            setOnClickListener {
                quoteViewModel.thumbsDownButtonClicked()
            }
        }

        shareImageView.setButtonFade()
        favoriteImageView.setButtonFade()
        thumbsDownImageView.setButtonFade()
        thumbsUpImageView.setButtonFade()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    if (quoteDetailsAction is QuoteDetailsAction.FavoritesAdd) {
                        favoriteImageView.setImageResource(R.drawable.ic_star_black_24dp)
                    }
                    else if (quoteDetailsAction is QuoteDetailsAction.FavoritesRemove) {
                        favoriteImageView.setImageResource(R.drawable.ic_star_border_black_24dp)
                    }
                }
            }
        )

        quoteViewModel.thumbsDownButtonClicked.observe(
            viewLifecycleOwner,
            Observer { event ->
                event.getContentIfNotHandled()?.let {quoteDetailsAction ->
                    val id = (quoteDetailsAction as QuoteDetailsAction.ThumbsDown).id
                    network.carpeDiemApi.downVote(id).enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d(SettingsViewModel.TAG, "onFailure:led to down vote")
                        }

                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Log.d(SettingsViewModel.TAG, "onResponse: down vote")
                            //show success
                            Snackbar.make(mainLayout, getString(R.string.down_vote_snack), Snackbar.LENGTH_LONG).show()
                        }
                    })
                }
            })

        quoteViewModel.thumbsUpButtonClicked.observe(
            viewLifecycleOwner,
            Observer { event ->
                event.getContentIfNotHandled()?.let { quoteDetailsAction ->
                    val id = (quoteDetailsAction as QuoteDetailsAction.ThumbsUp).id
                    network.carpeDiemApi.upVote(id).enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d(SettingsViewModel.TAG, "onFailure:led to up vote")
                        }

                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Log.d(SettingsViewModel.TAG, "onResponse: up vote")
                            //show success
                            Snackbar.make(mainLayout, getString(R.string.up_vote_snack), Snackbar.LENGTH_LONG).show()
                        }
                    })
                }
            })
    }

    private fun getFavoriteImageView(isFavorite: Boolean) = if (isFavorite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp

}
