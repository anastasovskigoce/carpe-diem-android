package com.alanford.carpediem.quotedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.alanford.carpediem.R
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.databinding.QuoteDetailBinding
import com.alanford.carpediem.quotelist.QuoteListViewModel

/**
 *  This Fragment will display the quote details
 */
class QuoteFragment : Fragment() {

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
        val binding = DataBindingUtil.inflate<QuoteDetailBinding>(
            layoutInflater,
            R.layout.quote_detail,
            container,
            false
        )
        binding.viewModel = quoteViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
