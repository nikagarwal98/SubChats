package com.example.subchats

class Message {
    var message: String? = null
    var subchatName: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(message: String?, subchatId: String?, senderId: String?) {
        this.message = message
        this.subchatName = subchatId
        this.senderId = senderId
    }
}