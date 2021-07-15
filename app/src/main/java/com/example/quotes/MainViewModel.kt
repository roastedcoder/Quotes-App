package com.example.quotes

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson

class MainViewModel(private val context: Context, private val onStartIndex: Int): ViewModel() {

    private var quotes: Array<Quote> = emptyArray()
    private var index: Int = 0
    private var size: Int = 0

    init {
        quotes = loadQuotesFromAssets()
        size = quotes.size

        index = onStartIndex
    }

    private fun loadQuotesFromAssets(): Array<Quote> {
        val inputStream = context.assets.open("quotes.json")
        val currSize = inputStream.available()
        val buffer = ByteArray(currSize)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, Charsets.UTF_8)

        val gson = Gson()
        return gson.fromJson(json, Array <Quote>::class.java)
    }

    fun getQuote(): TmpQuote {
        return TmpQuote(quotes[index].text, quotes[index].author, index +1)
    }

    fun nextQuote(): TmpQuote {
        (index++)%size
        return TmpQuote(quotes[index].text, quotes[index].author, index +1)
    }

    fun previousQuote(): TmpQuote {
        index -= 1-size
        index %= size
        return TmpQuote(quotes[index].text, quotes[index].author, index +1)
    }

    fun getIndex() = index
    fun getSize() = size
}