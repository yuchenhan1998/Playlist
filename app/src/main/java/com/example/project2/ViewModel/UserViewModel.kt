package com.example.project2.ViewModel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.project2.model.Tracks
import com.example.project2.model.user
import com.example.project2.util.QueryUtils

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var _userList: MutableLiveData<ArrayList<user>> = MutableLiveData()
    private var _trackList: MutableLiveData<ArrayList<Tracks>> = MutableLiveData()

    fun getUsersByQueryText(query: String): MutableLiveData<ArrayList<user>> {
        Log.e("Debug", "$query")
        loadUser("?method=user.getinfo&user=$query&api_key=3591fb8589f8a2337b30e12f57b42b52&format=json")
        return _userList
    }

    fun getUserTracksByQueryText(query: String): MutableLiveData<ArrayList<Tracks>> {
        Log.e("Debug", "$query")
        loadTracks("?method=user.gettoptracks&user=$query&api_key=3591fb8589f8a2337b30e12f57b42b52&format=json")
        return _trackList
    }

    private fun loadUser(query: String) {
        ArtistAsyncTask().execute(query)
    }

    private fun loadTracks(query: String) {
        TracksAsyncTask().execute(query)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ArtistAsyncTask : AsyncTask<String, Unit, ArrayList<user>>() {
        override fun doInBackground(vararg params: String?): ArrayList<user>? {
            return QueryUtils.fetchUserData(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<user>?) {
            if (result == null) {
                    Log.e("RESULTS", "No Results Found")
            } else {
                Log.e("RESULTS", result.toString())
                _userList.value = result
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class TracksAsyncTask : AsyncTask<String, Unit, ArrayList<Tracks>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Tracks>? {
            return QueryUtils.fetchUserTracks(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<Tracks>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            } else {
                Log.e("RESULTS", result.toString())
                _trackList.value = result
            }
        }
    }
}
