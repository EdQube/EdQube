package com.module.edqube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.module.edqube.R
import com.module.edqube.adapters.PostAdapter
import com.module.edqube.models.PostItem

class PostFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PostItem>()

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh)
        recyclerView = view.findViewById(R.id.postList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        postAdapter = PostAdapter(requireContext(), postList)
        recyclerView.adapter = postAdapter

        fetchPosts()

        swipeRefreshLayout.setOnRefreshListener {
            fetchPosts()
        }
    }

    private fun fetchPosts() {
        swipeRefreshLayout.isRefreshing = true
        postList.clear()

        firestore.collection("posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val userId = document.getString("userId") ?: continue
                    val type = document.getString("type") ?: continue
                    val content = document.getString("content") ?: ""

                    // Fetch user info by userId
                    firestore.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener { userDoc ->
                            val userName = userDoc.getString("name") ?: "Unknown"
                            val avatarUrl = userDoc.getString("profilePictureUrl") ?: ""

                            when (type) {
                                "text" -> {
                                    postList.add(
                                        PostItem.TextPost(
                                            user = userName,
                                            avatar = avatarUrl,
                                            content = content
                                        )
                                    )
                                }

                                "image" -> {
                                    val imageUrls = document.get("imageUrls") as? List<String> ?: emptyList()
                                    postList.add(
                                        PostItem.ImageTextPost(
                                            user = userName,
                                            avatar = avatarUrl,
                                            content = content,
                                            imageUrls = imageUrls
                                        )
                                    )
                                }

                                "poll" -> {
                                    val question = document.getString("question") ?: ""
                                    val options = document.get("options") as? List<String> ?: emptyList()
                                    val voteCounts = document.get("voteCounts") as? List<Long>
                                        ?: List(options.size) { 0L }

                                    postList.add(
                                        PostItem.PollPost(
                                            user = userName,
                                            avatar = avatarUrl,
                                            question = question,
                                            options = options,
                                            voteCounts = voteCounts.map { it.toInt() }.toMutableList()
                                        )
                                    )
                                }
                            }

                            postAdapter.notifyDataSetChanged()
                            swipeRefreshLayout.isRefreshing = false
                        }
                        .addOnFailureListener {
                            swipeRefreshLayout.isRefreshing = false
                        }
                }
            }
            .addOnFailureListener {
                swipeRefreshLayout.isRefreshing = false
            }
    }
}
