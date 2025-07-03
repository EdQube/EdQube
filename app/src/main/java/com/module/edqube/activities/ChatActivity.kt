package com.module.edqube.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.edqube.adapters.ChatAdapter
import com.module.edqube.databinding.ActivityChatBinding   // generated binding class
import com.module.edqube.models.ChatMessage
import androidx.core.view.isVisible

class ChatActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME = "extra_user_name"
    }

    private lateinit var binding: ActivityChatBinding     // view‑binding variable
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* inflate the layout via binding */
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /* 2️⃣  Toggle attachment panel */
        binding.btnAttach.setOnClickListener {
            val panelRoot = binding.attachPanel.root     // ⬅ root View of the include
            panelRoot.isVisible = !panelRoot.isVisible
        }

        /* 3️⃣  RecyclerView setup */
        chatAdapter = ChatAdapter(messageList)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply { stackFromEnd = true }
            adapter = chatAdapter
        }

        /* 4️⃣  Dummy data */
        messageList.addAll(
            listOf(
                ChatMessage("Hey userName, are you free?",  "10:10", true),
                ChatMessage("Yes, what's up?",                "10:11", false),
                ChatMessage("Need help with the quest plan.", "10:12", true),
                ChatMessage("Sure, let's discuss.",           "10:13", false),
                ChatMessage("Awesome, thanks!",               "10:14", true)
            )
        )
        chatAdapter.notifyDataSetChanged()
        binding.rvMessages.scrollToPosition(messageList.size - 1)
    }
}
