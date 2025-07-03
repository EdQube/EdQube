package com.module.edqube.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.module.edqube.R
import com.module.edqube.adapters.PostAdapter
import com.module.edqube.models.PostItem

class PostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false) // ✅ Your layout with SwipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        recyclerView = view.findViewById(R.id.postList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        postAdapter = PostAdapter(requireContext(), postList)
        recyclerView.adapter = postAdapter

        setupPosts()

        swipeRefreshLayout.setOnRefreshListener {
            refreshPosts()
        }
    }

    private fun setupPosts() {
        postList.clear()
        postList.add(
            PostItem.TextPost(
                user = "Mukesh Bhaiya",
                avatar = R.drawable.libr,
                content = "This is a pure text post. Welcome to the Edqube community!"
            )
        )

        postList.add(
            PostItem.ImageTextPost(
                user = "Jatin",
                avatar = R.drawable.coaching,
                content = "Checkout these cool designs!",
                imageUrls = listOf(
                    "https://media.istockphoto.com/id/814423752/photo/eye-of-model-with-colorful-art-make-up-close-up.jpg?s=612x612&w=0&k=20&c=l15OdMWjgCKycMMShP8UK94ELVlEGvt7GmB_esHWPYE=",
                    "https://images.unsplash.com/photo-1575936123452-b67c3203c357?fm=jpg&q=60&w=3000&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aW1hZ2V8ZW58MHx8MHx8fDA%3D"
                )
            )
        )

        postList.add(
            PostItem.PollPost(
                user = "Rohit",
                avatar = R.drawable.libr,
                question = "Which is your favorite app?",
                options = listOf("Instagram", "YouTube", "Telegram")
            )
        )

        postAdapter.notifyDataSetChanged()
    }

    private fun refreshPosts() {
        swipeRefreshLayout.isRefreshing = true

        // Simulate a network/API delay
        Handler().postDelayed({
            setupPosts() // re-fetch or reload posts
            swipeRefreshLayout.isRefreshing = false
        }, 1500)
    }
}
