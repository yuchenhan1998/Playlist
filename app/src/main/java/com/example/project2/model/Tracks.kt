package com.example.project2.model

import java.io.Serializable
//structure of a track
class Tracks(): Serializable {
    private var id: String = ""
    private var title: String = ""
    private var listeners: String = ""
    private var playcount: String = ""
    private var artist: String = ""
    private var image: ArrayList<String> = ArrayList()
    var isFavorite: Boolean = false

    constructor(
        title: String = "",
        playcount: String = "",
        listeners: String = "",
        artist: String = "",
        image: ArrayList<String>
    ): this() {
        this.title = title
        this.playcount = playcount
        this.listeners = listeners
        this.artist = artist
        this.image = image
    }

    //                        //
    // ------ Getters ------- //
    //                        //

    fun getTitle(): String {
        return this.title
    }


    fun getArtist(): String {
        return this.artist
    }

    fun getPlaycount(): String {
        return this.playcount
    }

    fun getListners(): String {
        return this.listeners
    }

    fun getImage(): ArrayList<String> {
        return this.image
    }

    fun getId(): String {
        return this.id
    }

    fun setID(ID: String){
        this.id = ID
    }
}