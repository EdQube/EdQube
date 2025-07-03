package com.module.edqube.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.module.edqube.R
import com.module.edqube.adapters.MessageAdapter
import com.module.edqube.models.Message

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val recyclerView = findViewById<RecyclerView>(R.id.messagesRecyclerView)
        val fab = findViewById<FloatingActionButton>(R.id.newMessageButton)

        val messages = listOf(
            Message("Liam Payne", "I will be the allowed!", "20:59"),
            Message("Obito", "I'll work on it.", "20:00"),
            Message("Emma James", "You should talk!", "16:30"),
            Message("Noah Centineo", "Noah you are late today!", "15:43"),
            Message("Elijah", "I believe all things have thoughts.", "14:00"),
            Message("Jiraya Shina", "When are we training?", "11:59"),
            Message("Oliver Tim", "What a drag!", "Yesterday"),
            Message("Shifa K", "Please take this ointment.", "Yesterday"),
            Message("Nej", "Okay noted.", "30/12/22")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MessageAdapter(messages) { clicked ->
            // start ChatActivity and pass user’s name
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            // TODO: open “new message” screen
        }
    }
}
