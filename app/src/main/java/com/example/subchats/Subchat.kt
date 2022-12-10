package com.example.subchats

class Subchat {
    var chatname: String? = null
    var user1uid: String? = null
    var user2uid: String? = null

    constructor() {}

    constructor(chatname: String?, user1uid: String?, user2uid: String?) {
        this.chatname = chatname
        this.user1uid = user1uid
        this.user2uid = user2uid
    }
}