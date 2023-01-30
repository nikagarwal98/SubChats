package com.example.subchats

class Message : java.io.Serializable{
    var message: String? = null
    var subchatName: String? = null
    var senderId: String? = null
    var selected: Boolean= false

    constructor(){}

    constructor(message: String?, subchatId: String?, senderId: String?) {
        this.message = message
        this.subchatName = subchatId
        this.senderId = senderId
    }
}