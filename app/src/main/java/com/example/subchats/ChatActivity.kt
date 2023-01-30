package com.example.subchats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.Serializable

class ChatActivity : AppCompatActivity() {

    private val TAG = "ChatActivity"

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var btn_send : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    var receiverUid : String? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null

    var inSelectionMode : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val subchatName = intent.getStringExtra("subchatName")
        var selectedMessages: List<Message>? = null
        receiverUid = intent.getStringExtra("receiverUid")
        if (intent.getSerializableExtra("selectedMessages") != null) {
            Log.i(TAG, "selectedMessages")
            selectedMessages =  intent.getSerializableExtra("selectedMessages") as List<Message>
        } else {
            Log.i(TAG, "selectedMessages null")
        }

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        supportActionBar?.title = subchatName

        senderRoom = senderUid + subchatName + receiverUid
        receiverRoom = receiverUid + subchatName + senderUid

        if (selectedMessages != null) {
            for (message in selectedMessages) {
                val messageObject = Message(message.message, subchatName, senderUid)
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
            }
        }

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        btn_send = findViewById(R.id.chatSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        btn_send.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, subchatName, senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (inSelectionMode) {
            menuInflater.inflate(R.menu.move_to_subchat_menu, menu)
        } else {
            menuInflater.inflate(R.menu.menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent = Intent(this@ChatActivity, Login::class.java)
            finish()
            startActivity(intent)
        } else if (item.itemId == R.id.viewsubchats) {
            val intent = Intent(this@ChatActivity, SubchatActivity::class.java)
            intent.putExtra("receiverUid", receiverUid)
            startActivity(intent)
        } else if (item.itemId == R.id.selectionmode) {
            inSelectionMode = true;
            messageAdapter.onEnterSelectionMode()
            invalidateOptionsMenu()
        } else if (item.itemId == R.id.clearselection) {
            inSelectionMode = false;
            messageAdapter.onClearSelections()
            invalidateOptionsMenu()
        } else if (item.itemId == R.id.copytosubchat) {
            val intent = Intent(this@ChatActivity, SubchatActivity::class.java)
            val selectedMessages = messageList.filter { it.selected }
            intent.putExtra("receiverUid", receiverUid)
            intent.putExtra("selectedMessages", selectedMessages as Serializable)
//            val bundle = intent.extras
//            if (bundle != null) {
//                for (key in bundle.keySet()) {
//                    Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
//                }
//            }
            startActivity(intent)
        }
        return true
    }

}