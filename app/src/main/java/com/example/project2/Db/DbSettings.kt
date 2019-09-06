package com.example.project2.Db

import android.provider.BaseColumns

class DbSettings {
    // Basic settings for the database
    companion object {
        const val DB_NAME = "playlist.db"
        const val DB_VERSION = 5
    }

    class DBPlaylist: BaseColumns {
        companion object {
            const val TABLE = "playlist"
            const val ID = BaseColumns._ID
            const val COL_TITLE = "title"
            const val COL_ARTIST = "artist"
            const val COL_IMAGE = "image"
            const val COL_PLAYCOUNT = "playcount"
            const val COL_LISTENERS = "listeners"


        }
    }

}