package com.example.project2.model

import java.io.Serializable
// structure of an artist
class Artist(): Serializable {
    private var name: String = ""
    private var listeners: String = ""
    private var url: String = ""
    private var image:  ArrayList<String> = ArrayList()


    constructor(
        name: String,
        listeners: String,
        url: String,
        image: ArrayList<String>
    ) : this() {

        this.name = name
        this.listeners = listeners
        this.url = url
        this.image = image

    }
    fun getName(): String {
        return this.name
    }
    fun getListeners(): String {
        return this.listeners
    }
    fun getURL(): String = this.url
    fun getImage(): ArrayList<String> {
        return this.image
    }
}