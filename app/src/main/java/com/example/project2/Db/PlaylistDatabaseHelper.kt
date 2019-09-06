package com.example.project2.Db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.project2.Db.DbSettings
import com.example.project2.model.Tracks

class PlaylistDatabaseHelper(context:Context):
    SQLiteOpenHelper(context, DbSettings.DB_NAME, null, DbSettings.DB_VERSION){
    // Set up the database
    override fun onCreate(db: SQLiteDatabase?) {
        val createPlaylistTableQuery = "CREATE TABLE " + DbSettings.DBPlaylist.TABLE + " ( " +
                DbSettings.DBPlaylist.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSettings.DBPlaylist.COL_TITLE + " TEXT NULL, " +
                DbSettings.DBPlaylist.COL_ARTIST + " TEXT NULL, " +
                DbSettings.DBPlaylist.COL_IMAGE + " TEXT NULL, " +
                DbSettings.DBPlaylist.COL_PLAYCOUNT + " TEXT NULL, " +
                DbSettings.DBPlaylist.COL_LISTENERS + " TEXT NULL)";

        db?.execSQL(createPlaylistTableQuery);

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DbSettings.DBPlaylist.TABLE)
        onCreate(db)
    }


}