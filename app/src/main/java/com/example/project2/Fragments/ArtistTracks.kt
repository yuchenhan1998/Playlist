package com.example.project2.Fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.project2.Fragments.ArtistList.ArtistAdapter
import com.example.project2.MainActivity
import com.example.project2.R
import com.example.project2.TrackInfo
import com.example.project2.ViewModel.TopTrackViewModel
import com.example.project2.model.Tracks
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_artist_tracks.*
import kotlinx.android.synthetic.main.top_track_list.view.*

@SuppressLint("ValidFragment")
class ArtistTracks(context: Context, query: String) : Fragment() {

    private var adapter = ResultAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: TopTrackViewModel
    private var trackList: ArrayList<Tracks> = ArrayList()
    private var query1: String = query

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_artist_tracks, container, false)
    }

    override fun onStart() {
        super.onStart()
        query_text.text = "Tracks by: $query1"

        // set up recyclerView
        result_items_list.layoutManager = GridLayoutManager(parentContext, 2)
        viewModel = ViewModelProviders.of(this).get(TopTrackViewModel::class.java)

        val observer = Observer<ArrayList<Tracks>> {
            result_items_list.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return trackList[p0].getTitle() == trackList[p1].getTitle()
                }

                override fun getOldListSize(): Int {
                    return trackList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return trackList[p0] == trackList[p1]
                }
            })
            Log.e("toptrack","50")
            result.dispatchUpdatesTo(adapter)
            trackList = it ?: ArrayList()
        }
        // get tracks from api
        viewModel.getTracksByQueryText(query1).observe(this, observer)
    }

    inner class ResultAdapter: RecyclerView.Adapter<ResultAdapter.TrackViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrackViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.top_track_list, p0, false)
            return TrackViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: TrackViewHolder, p1: Int) {
            val track =trackList[p1]
            val trackImages = track.getImage()
            val index = trackImages.size - 1
            if (trackImages.size == 0|| trackImages[index].isEmpty()) {
                // Do nothing for now

            }
            else {
                Picasso.with(this@ArtistTracks.parentContext).load(trackImages[index]).into(p0.trackImg)
            }

            p0.trackTitle.text = track.getTitle()

            // on track selected start a new activity: TrackInfo
            p0.itemView.setOnClickListener {
                val intent = Intent(this@ArtistTracks.parentContext, TrackInfo::class.java)
                intent.putExtra("TRACK", track)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return trackList.size
        }

        inner class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var trackImg: ImageView = itemView.cover
            var trackTitle: TextView = itemView.title
        }
    }
}
