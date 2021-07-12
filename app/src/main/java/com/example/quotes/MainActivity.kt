package com.example.quotes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.typeOf
import kotlin.text.Typography.quote

class MainActivity : AppCompatActivity() {

    private lateinit var mainVM: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("quoteIndex", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        var onStartIndex = sharedPref.getInt("currentIndex", 0)
        if(onStartIndex < 0) onStartIndex = 0

        Log.d("main", "after $onStartIndex")

        mainVM = ViewModelProvider(this, MainViewModelFactory(applicationContext, onStartIndex))
                    .get(MainViewModel::class.java)

        setQuote(mainVM.getQuote())
    }
    private fun setQuote(quote: Quote) {
        quoteText.text = quote.text
        quoteAuthor.text = quote.author
    }

    fun onPrevious(view: View) {
        setQuote(mainVM.previousQuote())
    }

    fun onNext(view: View) {
        setQuote(mainVM.nextQuote())
    }

    fun onShare(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, mainVM.getQuote().text)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()

        val currentIndex = mainVM.getIndex()
        val sharedPref = getSharedPreferences("quoteIndex", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply {
            putInt("currentIndex", currentIndex)
            apply()
        }
    }
}