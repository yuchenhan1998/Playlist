package com.example.project2.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.project2.model.Artist
import com.example.project2.util.QueryUtils

class ArtistsViewModel(application: Application) : AndroidViewModel(application) {
    private var _artistsList: MutableLiveData<ArrayList<Artist>> = MutableLiveData()

    fun getArtistsByQueryText(query: String): MutableLiveData<ArrayList<Artist>> {
        Log.e("Debug", "$query")
        loadArtist("?method=artist.search&artist=$query&api_key=3591fb8589f8a2337b30e12f57b42b52&format=json")
        return _artistsList
    }


    private fun loadArtist(query: String) {
        ArtistAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ArtistAsyncTask : AsyncTask<String, Unit, ArrayList<Artist>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Artist>? {
            return QueryUtils.fetchArtistData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<Artist>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            } else {
                Log.e("RESULTS", result.toString())
                _artistsList.value = result
            }
        }
    }
}