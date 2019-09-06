package com.example.project2.Fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.project2.MainActivity
import com.example.project2.R
import com.example.project2.TrackInfo
import com.example.project2.ViewModel.ArtistsViewModel
import com.example.project2.ViewModel.TopTrackViewModel
import com.example.project2.model.Artist
import com.example.project2.model.Tracks
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.artist_list.view.*
import kotlinx.android.synthetic.main.fragment_artist_tracks.*
import kotlinx.android.synthetic.main.top_track_list.view.*

@SuppressLint("ValidFragment")
class ArtistList(context: Context, query: String) : Fragment() {

    private var adapter = ArtistAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: ArtistsViewModel
    private var artistList: ArrayList<Artist> = ArrayList()
    private  lateinit var  artistTrackList: RecyclerView
    private var query1: String = query

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.fragment_artist_list, container, false)
        artistTrackList = view!!.findViewById(R.id.artist_list)
        return view
    }
    override fun onStart() {
        super.onStart()
        val display = "Search: $query1"
        query_text.text = display

        // set up recyclerView
        artistTrackList.layoutManager = GridLayoutManager(parentContext, 2)
        viewModel = ViewModelProviders.of(this).get(ArtistsViewModel::class.java)

        //observe updates from the async task
        val observer = Observer<ArrayList<Artist>> {
            artistTrackList.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return artistList[p0].getName() == artistList[p1].getName()
                }

                override fun getOldListSize(): Int {
                    return artistList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return artistList[p0] == artistList[p1]
                }
            })
            result.dispatchUpdatesTo(adapter)
            artistList = it ?: ArrayList()
        }
        // get artist from api
        viewModel.getArtistsByQueryText(query1).observe(this, observer)
    }

    inner class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArtistViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.artist_list, p0, false)
            return ArtistViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: ArtistViewHolder, p1: Int) {
            val artist =artistList[p1]
            val artistImages = artist.getImage()
            val index = artistImages.size - 1
            if (artistImages.size == 0 || artistImages[index].isEmpty()) {
                // Do nothing for now

            }
            else {
                Picasso.with(this@ArtistList.parentContext).load(artistImages[index]).into(p0.artistImg)

            }
            p0.artistName.text = artist.getName()
            // on artist selected show top tracks by artisttracks fragment
            p0.itemView.setOnClickListener {
                (activity as MainActivity).fm.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frag_placeholder, ArtistTracks(this@ArtistList.parentContext, artist.getName()))
                    .commit()
            }
        }

        override fun getItemCount(): Int {
            return artistList.size
        }

        inner class ArtistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var artistImg: ImageView = itemView.artist_img
            var artistName: TextView = itemView.artist_name
        }

    }

}

