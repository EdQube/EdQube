package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R
import com.module.edqube.models.Message

class MessageAdapter(
    private val messages: List<Message>,
    private val onItemClick: (Message) -> Unit        // <-- callback
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.tvName)
        val messageText: TextView = view.findViewById(R.id.tvMessage)
        val timeText: TextView = view.findViewById(R.id.tvTime)
        val profileImage: ImageView = view.findViewById(R.id.ivAvatar)

        init {
            view.setOnClickListener {
                onItemClick(messages[adapterPosition])   // <-- propagate click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messages[position]
        holder.nameText.text = item.name
        holder.messageText.text = item.message
        holder.timeText.text = item.time
        // load profileImage with Glide/Picasso if you have a URL
    }

    override fun getItemCount() = messages.size
}
