package com.example.quotes

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.nio.charset.Charset

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

    fun getQuote() = quotes[index]

    fun nextQuote() = quotes[(++index)%size]

    fun previousQuote(): Quote {
        index -= 1-size
        index %= size
        return quotes[index]
    }

    fun getIndex() = index
}