package com.example.subchats

class User {
    var username: String? = null
    var email: String? = null
    var uid: String? = null
    var subchats : ArrayList<Subchat>? = null

    constructor() {}

    constructor(username: String?, email: String?, uid: String?) {
        this.username = username
        this.email = email
        this.uid = uid
        this.subchats = ArrayList()
    }
}