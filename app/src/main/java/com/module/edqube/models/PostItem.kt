package com.module.edqube.models

sealed class PostItem {
    data class TextPost(val user: String, val avatar: String, val content: String, val createdAt: String) : PostItem()
    data class ImageTextPost(
        val user: String,
        val avatar: String,
        val content: String,
        val createdAt: String,
        val imageUrls: List<String>
    ) : PostItem()

    data class PollPost(
        val user: String,
        val avatar: String,
        val question: String,
        val options: List<String>,
        var voteCounts: MutableList<Int> = MutableList(4) { 0 }, // Change 4 if dynamic
        val createdAt: String,
        var userVotedIndex: Int? = null
    ) : PostItem()
}
