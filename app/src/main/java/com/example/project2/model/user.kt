package com.example.project2.model

import java.io.Serializable
//structure of a user
class user(): Serializable {

        private var id: String = ""
        private var playlists: String = ""
        private var playcount: String = ""
        private var name: String = ""
        private var image: ArrayList<String> = ArrayList()

        constructor(
            name: String = "",
            playcount: String = "",
            playlists: String = "",
            image: ArrayList<String>
        ): this() {
            this.name = name
            this.playcount = playcount
            this.playlists = playlists
            this.image = image
        }

        //                        //
        // ------ Getters ------- //
        //                        //

        fun getName(): String {
            return this.name
        }

        fun getPlaycount(): String {
            return this.playcount
        }

        fun getplaylists(): String {
            return this.playlists
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