package com.example.subchats

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

class SubchatAdapter(val context : Context, val subchatList : ArrayList<Subchat>, val receiverUid : String?, val selectedMessages : List<Message>?) : RecyclerView.Adapter<SubchatAdapter.SubchatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubchatViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.subchat_layout, parent, false)
        return SubchatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubchatViewHolder, position: Int) {
        val currentSubchat = subchatList[position]

        holder.chatname.text = currentSubchat.chatname

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("subchatName", currentSubchat.chatname)
            intent.putExtra("receiverUid", receiverUid)
            if (selectedMessages != null)
                intent.putExtra("selectedMessages", selectedMessages as Serializable)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return subchatList.size
    }

    class SubchatViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val chatname = itemView.findViewById<TextView>(R.id.chatname)
    }

}