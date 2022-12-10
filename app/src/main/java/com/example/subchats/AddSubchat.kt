package com.example.subchats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddSubchat : AppCompatActivity() {

    private val TAG = "AddSubchat"

    private lateinit var chatname: EditText
    private lateinit var btn_addsubchat: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    var receiverUid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subchat)

        receiverUid = intent.getStringExtra("receiverUid")
        Log.i(TAG, receiverUid!!)

        mAuth = FirebaseAuth.getInstance()
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        chatname = findViewById(R.id.chatname)

        btn_addsubchat = findViewById(R.id.btn_addsubchat)

        btn_addsubchat.setOnClickListener {
            val chatname = chatname.text.toString()

            addChatName(chatname, senderUid, receiverUid)
        }

    }

    private fun addChatName(chatname: String, senderUid: String?, receiverUid: String?) {
        mDbRef = FirebaseDatabase.getInstance().reference
        var subchatObject = Subchat(chatname, senderUid, receiverUid)
        mDbRef.child("subchats").child(chatname).setValue(subchatObject)
        val intent = Intent(this@AddSubchat, SubchatActivity::class.java)
        intent.putExtra("receiverUid", receiverUid)
        finish()
        startActivity(intent)
    }
}