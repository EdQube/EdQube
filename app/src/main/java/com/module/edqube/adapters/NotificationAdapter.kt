package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R
import com.module.edqube.models.Notification

class NotificationAdapter(
    private val notifications: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val notificationText: TextView = itemView.findViewById(R.id.notificationText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notifications[position]
        holder.profileImage.setImageResource(item.profileImageRes)
        holder.notificationText.text = item.text
        holder.timeText.text = item.time
        if (item.isUnread) {
            holder.itemView.setBackgroundColor(0xFFEAF6F9.toInt()) // light blue
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF.toInt()) // white
        }
    }

    override fun getItemCount() = notifications.size
}
