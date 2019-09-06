package com.example.project2.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.provider.BaseColumns
import android.util.Log
import com.example.project2.Db.DbSettings
import com.example.project2.Db.PlaylistDatabaseHelper
import com.example.project2.model.Tracks
import com.example.project2.util.QueryUtils

class TopTrackViewModel(application: Application): AndroidViewModel(application) {
    private var _toptracksList: MutableLiveData<ArrayList<Tracks>> = MutableLiveData()
    private var _playlistDBHelper: PlaylistDatabaseHelper = PlaylistDatabaseHelper(application)

    fun getTopTracks(): MutableLiveData<ArrayList<Tracks>> {
        loadTracks("?method=chart.gettoptracks&api_key=3591fb8589f8a2337b30e12f57b42b52&format=json")
        return _toptracksList
    }

    fun getTracksByQueryText(query: String): MutableLiveData<ArrayList<Tracks>> {
        loadTracks_Artist("?method=artist.gettoptracks&artist=$query&api_key=3591fb8589f8a2337b30e12f57b42b52&format=json")
        return _toptracksList
    }

    private fun loadTracks_Artist(query: String) {
        TracksByArtistAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    inner class TracksByArtistAsyncTask : AsyncTask<String, Unit, ArrayList<Tracks>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Tracks>? {
            return QueryUtils.fetchArtistTracksData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<Tracks>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            } else {
                Log.e("RESULTS", result.toString())
                //Tag Items with favorites
                val fromPlaylist = loadSavedTracks()
                var newPlaylist: ArrayList<Tracks> = ArrayList()
                for (i in result){
                    for (j in fromPlaylist){
                        if (i.getTitle() == j.getTitle()) {
                            i.isFavorite = true;
                            break;
                        }
                    }
                    newPlaylist.add(i)
                }
                _toptracksList.value = newPlaylist
            }
        }
    }

    private fun loadTracks(query: String) {
        TrackAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    inner class TrackAsyncTask : AsyncTask<String, Unit, ArrayList<Tracks>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Tracks>? {
            return QueryUtils.fetchTopTracksData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<Tracks>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            } else {
                Log.e("RESULTS", result.toString())
                val fromPlaylist = loadSavedTracks()
                var newPlaylist: ArrayList<Tracks> = ArrayList()
                for (i in result){
                    for (j in fromPlaylist){
                        if (i.getTitle() == j.getTitle()) {
                            i.isFavorite = true;
                            break;
                        }
                    }
                    newPlaylist.add(i)
                }
                _toptracksList.value = newPlaylist
            }
        }
    }

    fun getPlaylist(): MutableLiveData<ArrayList<Tracks>> {
        val returnList = this.loadSavedTracks()
        this._toptracksList.value = returnList
        return this._toptracksList
    }

    fun loadSavedTracks(): ArrayList<Tracks>{

        val db = this._playlistDBHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            DbSettings.DBPlaylist.COL_TITLE,
            DbSettings.DBPlaylist.COL_LISTENERS,
            DbSettings.DBPlaylist.COL_PLAYCOUNT,
            DbSettings.DBPlaylist.COL_ARTIST,
            DbSettings.DBPlaylist.COL_IMAGE

        )

        val cursor = db.query(
            DbSettings.DBPlaylist.TABLE,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        val saved = ArrayList<Tracks>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.COL_TITLE))
                val listeners = getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.COL_LISTENERS))
                val playcount = getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.COL_PLAYCOUNT))
                val artist = getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.COL_ARTIST))
                val image = getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.COL_IMAGE))
                val imageArrayList = kotlin.collections.arrayListOf(image)

                val theTrack =
                    com.example.project2.model.Tracks(title, playcount,listeners, artist, imageArrayList)
                theTrack.setID(getString(getColumnIndexOrThrow(com.example.project2.Db.DbSettings.DBPlaylist.ID)))
                theTrack.isFavorite = true
                saved.add(theTrack)
            }
        }
        db.close()
        cursor.close()
        return saved;
    }
    // add track to the playlist
    fun addTrack(song: Tracks) {
        // make database writable
        val db: SQLiteDatabase = this._playlistDBHelper.writableDatabase

        // Create a new map of values, with column names as keys
        val values = ContentValues().apply {
            put(DbSettings.DBPlaylist.COL_TITLE, song.getTitle())
            put(DbSettings.DBPlaylist.COL_LISTENERS, song.getListners())
            put(DbSettings.DBPlaylist.COL_PLAYCOUNT, song.getPlaycount())
            put(DbSettings.DBPlaylist.COL_ARTIST, song.getArtist())
            put(DbSettings.DBPlaylist.COL_IMAGE, song.getImage()[song.getImage().size - 1])
        }

        // new row ID
        db?.insert(DbSettings.DBPlaylist.TABLE, null, values)
    }
    // remove track from the playlist
    fun removeTrack(id: String,  isFromResultList: Boolean = false) {
        val database: SQLiteDatabase = this._playlistDBHelper.writableDatabase
        val id_array= arrayOf(id)
        database.delete(DbSettings.DBPlaylist.TABLE,"${DbSettings.DBPlaylist.COL_TITLE}=?",id_array)
        database.close()
        var  product_ :Tracks=Tracks()
        var copy=_toptracksList.value
        if(copy!=null){
            for(product in copy){
                product_=product
                if(id==product.getTitle()) {
                    break
                }
            }
            if(isFromResultList){
                product_.isFavorite=false
            }
            else{
                copy.remove(product_)
            }
            _toptracksList.value = copy
        }
    }
}