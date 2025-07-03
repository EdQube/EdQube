package com.module.edqube.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R
import com.module.edqube.adapters.NotificationAdapter
import com.module.edqube.models.Notification

class NotificationActivity : AppCompatActivity() {

    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val recyclerView = findViewById<RecyclerView>(R.id.notificationsRecyclerView)
        val backIcon = findViewById<ImageView>(R.id.backIcon)

        val notifications = listOf(
            Notification(R.drawable.libr, "Aman Shulka liked your post.", "9:56 AM", true),
            Notification(R.drawable.user, "Anunay Joshi commented on your post.", "9:56 AM", true),
            Notification(R.drawable.profile, "Anunay Joshi liked your post.", "9:56 AM", false),
            Notification(R.drawable.libr, "Mukesh Library Your subscription expiring today.", "9:56 AM", false),
            Notification(R.drawable.profile, "Rudra Pratap commented on your post.", "9:56 AM", false),
            Notification(R.drawable.coaching, "Mukesh Library has opened near you! Explore a quiet, focused space for your study sessions!", "9:56 AM", false),
            Notification(R.drawable.libr, "Shreya Sinha mentioned you in a comment.", "9:56 AM", false)
        )

        adapter = NotificationAdapter(notifications)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        backIcon.setOnClickListener {
            finish()
        }
    }
}
