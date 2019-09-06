package com.example.project2.util

import android.text.TextUtils
import android.util.Log
import com.example.project2.model.Artist
import com.example.project2.model.Tracks
import com.example.project2.model.user
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class QueryUtils {
    companion object {
        private val LogTag = this::class.java.simpleName
        private const val BaseURL =  "http://ws.audioscrobbler.com/2.0/" // localhost URL

        fun fetchTopTracksData(jsonQueryString: String): ArrayList<Tracks>? {
            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extract1(jsonResponse)
        }
        fun fetchArtistTracksData(jsonQueryString: String): ArrayList<Tracks>? {
            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extract2(jsonResponse)
        }


//        fun fetchTrackInfoData(jsonQueryString: String): Tracks? {
//            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")
//
//            var jsonResponse: String? = null
//            try {
//                jsonResponse = makeHttpRequest(url)
//            } catch (e: IOException) {
//                Log.e(this.LogTag, "Problem making the HTTP request.", e)
//            }
//            return extract3(jsonResponse)
//        }

        fun fetchArtistData(jsonQueryString: String): ArrayList<Artist>? {
            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            } catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extract4(jsonResponse)
        }

        fun fetchUserData(jsonQueryString: String): ArrayList<user>? {
            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            } catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extract5(jsonResponse)
        }

        fun fetchUserTracks(jsonQueryString: String): ArrayList<Tracks>? {
            val url: URL? = createUrl("${this.BaseURL}$jsonQueryString")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            } catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extract6(jsonResponse)
        }

        private fun createUrl(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            }
            catch (e: MalformedURLException) {
                Log.e(this.LogTag, "Problem building the URL.", e)
            }

            return url
        }

        private fun makeHttpRequest(url: URL?): String {
            var jsonResponse = ""

            if (url == null) {
                return jsonResponse
            }

            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 10000 // 10 seconds
                urlConnection.connectTimeout = 15000 // 15 seconds
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                }
                else {
                    Log.e(this.LogTag, "Error response code: ${urlConnection.responseCode}")
                }
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem retrieving the product data results: $url", e)
            }
            finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }

            return jsonResponse
        }

        private fun readFromStream(inputStream: InputStream?): String {
            val output = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    output.append(line)
                    line = reader.readLine()
                }
            }

            return output.toString()
        }

        private fun extract1(trackJson: String?): ArrayList<Tracks>? {
            if (TextUtils.isEmpty(trackJson)) {
                return null
            }

            val TopTrackList = ArrayList<Tracks>()
            try {
                val trackObject = JSONObject(trackJson)
                val tracks = trackObject.getJSONObject("tracks")
                val trackArray = tracks.getJSONArray("track")
                for (i in 0 until trackArray.length()) {
                    val track = trackArray.getJSONObject(i)

                    // Images
                    val images = returnValueOrDefault<JSONArray>(track, "image") as JSONArray?
                    val imageArrayList = ArrayList<String>()
                    if (images != null) {
                        for (j in 0 until images.length()) {
                            val image = images.getJSONObject(j);
                            imageArrayList.add(returnValueOrDefault<String>(image, "#text") as String)
                        }
                    }

                    // Artists
                    val artists = track.getJSONObject("artist")
                    val artistname= returnValueOrDefault<String>(artists, "name") as String

                    TopTrackList.add(Tracks(
                        returnValueOrDefault<String>(track, "name") as String,
                        returnValueOrDefault<String>(track, "playcount") as String,
                        returnValueOrDefault<String>(track, "listeners") as String,
                        artistname,
                        imageArrayList
                    ))
                }
            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the track JSON results", e)
            }
            return TopTrackList;
        }

        private fun extract2(trackJson: String?): ArrayList<Tracks>? {
            if (TextUtils.isEmpty(trackJson)) {
                return null
            }

            val artistTrackList = ArrayList<Tracks>()
            try {
                val artistObject = JSONObject(trackJson)
                val tracks = artistObject.getJSONObject("toptracks")
                val trackArray = tracks.getJSONArray("track")
                for (i in 0 until trackArray.length()) {
                    val track = trackArray.getJSONObject(i)

                    // Images
                    val images = returnValueOrDefault<JSONArray>(track, "image") as JSONArray?
                    val imageArrayList = ArrayList<String>()
                    if (images != null) {
                        for (j in 0 until images.length()) {
                            val image = images.getJSONObject(j);
                            imageArrayList.add(returnValueOrDefault<String>(image, "#text") as String)
                        }
                    }

                    // Artists
                    val artists = track.getJSONObject("artist")
                    val artistname= returnValueOrDefault<String>(artists, "name") as String

                    artistTrackList.add(Tracks(
                        returnValueOrDefault<String>(track, "name") as String,
                        returnValueOrDefault<String>(track, "playcount") as String,
                        returnValueOrDefault<String>(track, "listeners") as String,
                        artistname,
                        imageArrayList
                    ))
                }
            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the track JSON results", e)
            }

            return artistTrackList;
        }


        private fun extract4(ArtistJson: String?): ArrayList<Artist>? {
            if (TextUtils.isEmpty(ArtistJson)) {
                return null
            }

            val artists = ArrayList<Artist>()
            try {

                val artistObject = JSONObject(ArtistJson)
                val resultObject = artistObject.getJSONObject("results")
                val artistMatches = resultObject.getJSONObject("artistmatches")
                val artistArray = artistMatches.getJSONArray("artist")
                for (i in 0 until artistArray.length()) {
                    val thisArtist = artistArray.getJSONObject(i)

                    // Images
                    val images = returnValueOrDefault<JSONArray>(thisArtist, "image") as JSONArray?
                    val imageArrayList = ArrayList<String>()
                    if (images != null) {
                        for (j in 0 until images.length()) {
                            val thisImage = images.getJSONObject(j)
                            imageArrayList.add(returnValueOrDefault<String>(thisImage, "#text") as String)
                        }
                    }

                    artists.add(
                        Artist(
                            returnValueOrDefault<String>(thisArtist, "name") as String,
                            returnValueOrDefault<String>(thisArtist, "listeners") as String,
                            returnValueOrDefault<String>(thisArtist, "url") as String,
                            imageArrayList
                        )
                    )

                }

            } catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the product JSON results", e)
            }

            return artists
        }

        private fun extract5(UserJson: String?): ArrayList<user>? {
            if (TextUtils.isEmpty(UserJson)) {
                return null
            }

            val users = ArrayList<user>()
            try {

                val userObject = JSONObject(UserJson)
                val resultObject = userObject.getJSONObject("user")
                //image
                val images = resultObject.getJSONArray("image")
                val imageArrayList = ArrayList<String>()
                if (images != null) {
                    for (j in 0 until images.length()) {
                        val thisImage = images.getJSONObject(j)
                        imageArrayList.add(returnValueOrDefault<String>(thisImage, "#text") as String)
                    }
                }

                users.add(
                    user(
                        returnValueOrDefault<String>(resultObject, "name") as String,
                        returnValueOrDefault<String>(resultObject, "playcount") as String,
                        returnValueOrDefault<String>(resultObject, "playlists") as String,
                        imageArrayList
                    )
                )


            } catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the product JSON results", e)
            }

            return users
        }
        private fun extract6(trackJson: String?): ArrayList<Tracks>? {
            if (TextUtils.isEmpty(trackJson)) {
                return null
            }

            val userTrackList = ArrayList<Tracks>()
            try {
                val artistObject = JSONObject(trackJson)
                val tracks = artistObject.getJSONObject("toptracks")
                val trackArray = tracks.getJSONArray("track")
                for (i in 0 until trackArray.length()) {
                    val track = trackArray.getJSONObject(i)

                    // Images
                    val images = returnValueOrDefault<JSONArray>(track, "image") as JSONArray?
                    val imageArrayList = ArrayList<String>()
                    if (images != null) {
                        for (j in 0 until images.length()) {
                            val image = images.getJSONObject(j);
                            imageArrayList.add(returnValueOrDefault<String>(image, "#text") as String)
                        }
                    }

                    // Artists
                    val artists = track.getJSONObject("artist")
                    val artistname= returnValueOrDefault<String>(artists, "name") as String

                    userTrackList.add(Tracks(
                        returnValueOrDefault<String>(track, "name") as String,
                        returnValueOrDefault<String>(track, "playcount") as String,
                        returnValueOrDefault<String>(track, "listeners") as String,
                        artistname,
                        imageArrayList
                    ))
                }
            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the track JSON results", e)
            }

            return userTrackList;
        }
        private inline fun <reified T> returnValueOrDefault(json: JSONObject, key: String): Any? {
            when (T::class) {
                String::class -> {
                    return if (json.has(key)) {
                        json.getString(key)
                    } else {
                        ""
                    }
                }
                Int::class -> {
                    return if (json.has(key)) {
                        json.getInt(key)
                    }
                    else {
                        return -1
                    }
                }
                Double::class -> {
                    return if (json.has(key)) {
                        json.getDouble(key)
                    }
                    else {
                        return -1.0
                    }
                }
                Long::class -> {
                    return if (json.has(key)) {
                        json.getLong(key)
                    }
                    else {
                        return (-1).toLong()
                    }
                }
                JSONObject::class -> {
                    return if (json.has(key)) {
                        json.getJSONObject(key)
                    }
                    else {
                        return null
                    }
                }
                JSONArray::class -> {
                    return if (json.has(key)) {
                        json.getJSONArray(key)
                    }
                    else {
                        return null
                    }
                }
                else -> {
                    return null
                }
            }
        }
    }
}