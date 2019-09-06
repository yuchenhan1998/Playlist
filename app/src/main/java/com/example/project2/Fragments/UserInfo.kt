package com.example.project2.Fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.project2.MainActivity
import com.example.project2.R
import com.example.project2.ViewModel.ArtistsViewModel
import com.example.project2.ViewModel.UserViewModel
import com.example.project2.model.Artist
import com.example.project2.model.user
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.artist_list.view.*
import kotlinx.android.synthetic.main.fragment_artist_tracks.*
import kotlinx.android.synthetic.main.user_list.view.*

@SuppressLint("ValidFragment")
class UserInfo(context: Context, query: String) : Fragment() {
    private var adapter = ArtistAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: UserViewModel
    private var userList: ArrayList<user> = ArrayList()
    private  lateinit var  userTrackList: RecyclerView
    private var query1: String = query

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.fragment_artist_list, container, false)
        userTrackList = view!!.findViewById(R.id.artist_list)
        return view
    }

    override fun onStart() {
        super.onStart()
        val display = "Search: $query1"
        query_text.text = display
        //set out recycler review
        userTrackList.layoutManager = LinearLayoutManager(parentContext)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        // oberve update from async task
        val observer = Observer<ArrayList<user>> {
            userTrackList.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return userList[p0].getName() == userList[p1].getName()
                }

                override fun getOldListSize(): Int {
                    return userList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return userList[p0] == userList[p1]
                }
            })
            result.dispatchUpdatesTo(adapter)
            userList = it ?: ArrayList()
        }
        // get user info from the api
        viewModel.getUsersByQueryText(query1).observe(this, observer)
    }

    inner class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArtistViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.user_list, p0, false)
            return ArtistViewHolder(itemView)
        }

        override fun onBindViewHolder(p0: ArtistViewHolder, p1: Int) {
            val user =userList[p1]
            val userImages = user.getImage()
            val index = userImages.size - 1
            if (userImages.size == 0 || userImages[index].isEmpty()) {
                // Do nothing for now
            }
            else {
                Picasso.with(this@UserInfo.parentContext).load(userImages[index]).into(p0.userImg)

            }
            p0.userName.text = "User: " + user.getName()
            p0.userPlaylists.text = "Playlists: " + user.getplaylists()
            p0.userPlaycount.text = "Playcount: " + user.getPlaycount()
            // on user selected show top tracks by usertracks fragment
            p0.itemView.setOnClickListener {
                (activity as MainActivity).fm.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.frag_placeholder1,UserTracks(this@UserInfo.parentContext, user.getName()))
                    .commit()
            }
        }

        override fun getItemCount(): Int {
            return userList.size
        }

        inner class ArtistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var userImg: ImageView = itemView.user_img
            var userName: TextView = itemView.user_name
            var userPlaylists: TextView = itemView.user_playlists
            var userPlaycount: TextView = itemView.user_playcount
        }
    }
}
