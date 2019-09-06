package com.example.project2.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.project2.MainActivity

import com.example.project2.R
import kotlinx.android.synthetic.main.content_main.*

@SuppressLint("ValidFragment")
class Search(context: Context) : Fragment() {
    private var parentContext = context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View? = inflater.inflate(R.layout.fragment_search, container, false)
        // show tracks on top of the charts as home/starting page
        (activity as MainActivity).fm.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frag_placeholder, TopTracks(this.parentContext))
            .commit()
        // start search functionality
        val searchtext = view!!.findViewById<EditText>(R.id.search)
        searchtext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = search.text
                search.setText("")
                if (searchText.toString() == "") {
                    val toast = Toast.makeText(this.parentContext, "Please enter text", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    return@setOnEditorActionListener true
                } else {
                    performSearch(searchText.toString())
                    return@setOnEditorActionListener false
                }
            }

            return@setOnEditorActionListener false
        }
        return view;
    }

    private fun performSearch(query: String) {
        (activity as MainActivity).fm.beginTransaction()
            .replace(R.id.frag_placeholder, ArtistList(this.parentContext, query))
            .commit()
    }
}
