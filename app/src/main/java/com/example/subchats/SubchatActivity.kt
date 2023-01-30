package com.example.subchats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SubchatActivity : AppCompatActivity() {

    private val TAG = "SubchatActivity"

    private lateinit var subchatRecyclerView : RecyclerView
    private lateinit var subchatList : ArrayList<Subchat>
    private lateinit var subchatAdapter : SubchatAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    var receiverUid : String? = null
    var selectedMessages : List<Message>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subchat)

        receiverUid = intent.getStringExtra("receiverUid")

        if (intent.getSerializableExtra("selectedMessages") != null) {
            Log.i(TAG, "selectedMessages")
            selectedMessages =  intent.getSerializableExtra("selectedMessages") as List<Message>
        } else {
            Log.i(TAG, "selectedMessages null")
        }

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        subchatList = ArrayList()
        subchatAdapter = SubchatAdapter(this, subchatList, receiverUid, selectedMessages)
        subchatRecyclerView = findViewById(R.id.subchatRecyclerView)

        subchatRecyclerView.layoutManager = LinearLayoutManager(this)
        subchatRecyclerView.adapter = subchatAdapter


        mDbRef.child("subchats").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                subchatList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentSubchat = postSnapshot.getValue(Subchat::class.java)

                    if ((mAuth.currentUser?.uid == currentSubchat?.user1uid || mAuth.currentUser?.uid == currentSubchat?.user2uid) && (receiverUid == currentSubchat?.user1uid || receiverUid == currentSubchat?.user2uid)) {
                        subchatList.add(currentSubchat!!)
                    }
                }
                subchatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.subchatmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent = Intent(this@SubchatActivity, Login::class.java)
            finish()
            startActivity(intent)
        } else if (item.itemId == R.id.addsubchat) {
            Log.i(TAG, receiverUid!!)
            val intent = Intent(this@SubchatActivity, AddSubchat::class.java)
            intent.putExtra("receiverUid", receiverUid)
            startActivity(intent)
        }
        return true
    }
}