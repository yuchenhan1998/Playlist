package com.example.project2.Fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.project2.R
import com.example.project2.TrackInfo
import com.example.project2.ViewModel.TopTrackViewModel
import com.example.project2.model.Tracks
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_play_list.*
import kotlinx.android.synthetic.main.top_track_list.view.*
import java.util.ArrayList


@SuppressLint("ValidFragment")
class PlayList(context: Context): Fragment() {
    private var adapter = PlaylistAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: TopTrackViewModel

    private var playList: ArrayList<Tracks> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_play_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        // set up recycler view
        playlist.layoutManager = LinearLayoutManager(parentContext)
        viewModel = ViewModelProviders.of(this).get(TopTrackViewModel::class.java)

        // observe update from the async task
        val observer = Observer<ArrayList<Tracks>> {
            playlist.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    if (p0 >= playList.size || p1 >= playList.size) {
                        return false
                    }
                    return playList[p0].getTitle() == playList[p1].getTitle()
                }

                override fun getOldListSize(): Int {
                    return playList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return (playList[p0] == playList[p1])
                }
            })
            result.dispatchUpdatesTo(adapter)
            playList = it ?: ArrayList()
        }
        // get the playlist data from the database
        viewModel.getPlaylist().observe(this, observer)
    }

    inner class PlaylistAdapter: RecyclerView.Adapter<PlaylistAdapter.TrackViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int):  TrackViewHolder{
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.top_track_list, p0, false)
            return TrackViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: TrackViewHolder, p1: Int) {
            val track = playList[p1]
            val trackImages = track.getImage()
            if (trackImages.size == 0) {
                // Do nothing for now
            }
            else {
                Picasso.with(this@PlayList.parentContext).load(trackImages[0]).into(p0.trackImg)
            }
            p0.trackTitle.text = track.getTitle() + "      " + track.getArtist()
            // on track selected start a new activity: TrackInfo
            p0.itemView.setOnClickListener {
                val intent = Intent(this@PlayList.parentContext, TrackInfo::class.java)
                intent.putExtra("TRACK", track)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return playList.size
        }

        inner class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var trackImg: ImageView = itemView.cover
            var trackTitle: TextView = itemView.title
        }
    }
}