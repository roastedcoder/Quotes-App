package com.example.quotes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainVM: MainViewModel
    private var size: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("quoteIndex", Context.MODE_PRIVATE)

        var onStartIndex = sharedPref.getInt("currentIndex", 0)
        if(onStartIndex < 0) onStartIndex = 0

//        Log.d("main", "after $onStartIndex")

        mainVM = ViewModelProvider(this, MainViewModelFactory(applicationContext, onStartIndex))
                    .get(MainViewModel::class.java)
        setQuote(mainVM.getQuote())

        size = mainVM.getSize()

        var key = -1
        val searchView = layoutInflater.inflate(R.layout.search_dialog, null)
        val input = searchView.findViewById<EditText>(R.id.etInput)

        val searchAlert = AlertDialog.Builder(this)
                .setTitle("Enter ID")
                .setIcon(R.drawable.ic_search)
                .setView(searchView)
                .setPositiveButton("Search") { _, _ ->
                    var tmp = input.text.toString()
                    tmp.trim()
                    key = if(tmp.isNotEmpty()) tmp.toInt()
                    else -1
                    input.text.clear()
                    if(key in 0 until size) {
                        val searchVM = ViewModelProvider(this, SearchVMFactory(applicationContext, key))
                                .get(MainViewModel::class.java)

                        Toast.makeText(this, "coming soon...", Toast.LENGTH_SHORT).show()
                        setQuote(searchVM.getQuote())
                    }
                    else {
                        Toast.makeText(this, "coming soon...", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    input.text.clear()
                }
                .create()

        btnSearch.setOnClickListener {
            Toast.makeText(this, "coming soon...", Toast.LENGTH_SHORT).show()
//            searchAlert.show()
        }

    }
    private fun setQuote(tmpQuote: TmpQuote) {
        quoteText.text = tmpQuote.text
        if(tmpQuote.author == null) quoteAuthor.text = "Anonymous"
        else quoteAuthor.text = tmpQuote.author
        tvQuoteID.text = tmpQuote.id.toString()
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
        intent.putExtra(Intent.EXTRA_TEXT, tvQuoteID.text.toString() + ". " + quoteText.text.toString() + "- " + quoteAuthor.text.toString())
        val chooser = Intent.createChooser(intent, "Share this quote using...")
        startActivity(chooser)
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

    fun onSearch(view: View) {}
}