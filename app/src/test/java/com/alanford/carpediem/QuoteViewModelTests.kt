package com.alanford.carpediem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alanford.carpediem.data.Quote
import com.alanford.carpediem.quotedetails.QuoteViewModel
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner


// Test for the QuoteViewModel class
// Created by ganastasovski on 8/4/20.
// Copyright (c) 2020 alanford. All rights reserved.
//
@RunWith(MockitoJUnitRunner::class)
class QuoteViewModelTests {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private val quote: Quote = mock(Quote::class.java)

    private lateinit var subjectUnderTest: QuoteViewModel

    @Before
    fun setUp(){
        subjectUnderTest = QuoteViewModel()
        subjectUnderTest.loadQuote(quote)
    }


    @Test
    fun `Correct variable is used for author`(){
        assertThat(subjectUnderTest.quoteLiveData.value?.author, `is`(quote.author))
    }

    @Test
    fun `Correct variable is used for quote`(){
        assertThat(subjectUnderTest.quoteLiveData.value?.quoteText, `is`(quote.quoteText))
    }
}