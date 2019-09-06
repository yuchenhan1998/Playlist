package com.example.project2

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.*
import com.example.project2.ViewModel.TopTrackViewModel
import com.example.project2.model.Tracks
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_track_info.*

class TrackInfo : AppCompatActivity() {

    private lateinit var track: Tracks

    private lateinit var viewModel: TopTrackViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_info)

        // get track from intent
        track = intent.extras!!.getSerializable("TRACK") as Tracks
        viewModel = ViewModelProviders.of(this).get(TopTrackViewModel::class.java)

        // add track to db
        this.loadUI(track)
        val fab: View = findViewById(R.id.fab)
        // set actions on the fab and display important message
        fab.setOnClickListener {
            if (this.track.isFavorite){
                    viewModel.removeTrack(this.track.getTitle())
                    this.track.isFavorite = false;
                Snackbar.make(it,"${track.getTitle()} Deleted from Playlist Successfully", Snackbar.LENGTH_SHORT)
                    .show()
                }
                else{
                    viewModel.addTrack(this.track)
                    this.track.isFavorite = true;
                Snackbar.make(it,"${track.getTitle()} Added to Playlist Successfully", Snackbar.LENGTH_SHORT)
                    .show()
                }

        }
    }

    override fun onBackPressed() {
        this.finish()
    }
    // update UI
    @SuppressLint("SetTextI18n")
    private fun loadUI(trackDetails: Tracks) {

            val trackImages = trackDetails.getImage()
            val index = trackImages.size - 1
            if (trackImages.size == 0 || trackImages[index].isEmpty()) {
                // Do nothing
                // Images Array is either empty, or
                // the urls lead nowhere
            } else {

                // load, format, round corners of image
                Picasso.with(this)
                    .load(trackImages[index])
                    .transform(RoundedCornersTransformation(30, 10))
                    .resize(850, 850)
                    .into(img)
            }

        // display text
        track_title.text = trackDetails.getTitle()
        track_artist.text = trackDetails.getArtist()
        track_play_count.text = "Play Count: ${trackDetails.getPlaycount()} "
        track_listeners.text = "Listeners: ${trackDetails.getListners()} "

    }


    override fun onStart() {
        super.onStart()
    }



}
