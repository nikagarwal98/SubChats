package com.example.subchats

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context, val messageList : ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val TAG = "MessageAdapter"

    val ITEM_RECEIEVED = 1
    val ITEM_SENT = 2
    var inSelectionMode = false;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType == ITEM_RECEIEVED) {
            val view : View = LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            return ReceivedViewHolder(view)
        } else {
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        } else {
            val viewHolder = holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
        }
        holder.bind(currentMessage)

        Log.i(TAG, inSelectionMode.toString())
        if (inSelectionMode) {
            holder.itemView.setOnClickListener {
                currentMessage.selected = !currentMessage.selected
                notifyItemChanged(position)
            }
        } else {
            holder.itemView.setOnClickListener(null)
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        }
        return ITEM_RECEIEVED
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView : View) : MessageViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_message)
    }

    class ReceivedViewHolder(itemView : View) : MessageViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.received_message)
    }

    open class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(message: Message) {
            itemView.setBackgroundColor(if (message.selected) Color.LTGRAY else Color.WHITE)
        }
    }

    fun onClearSelections() {
        for (message in messageList) {
            message.selected = false
        }
        inSelectionMode = false
        notifyDataSetChanged()
    }

    fun onEnterSelectionMode() {
        inSelectionMode = true
        notifyDataSetChanged()
    }

}