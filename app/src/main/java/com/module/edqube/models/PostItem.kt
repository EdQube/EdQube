package com.module.edqube.models

sealed class PostItem {
    data class TextPost(val user: String, val avatar: Int, val content: String) : PostItem()
    data class ImageTextPost(
        val user: String,
        val avatar: Int,
        val content: String,
        val imageUrls: List<String>
    ) : PostItem()

    data class PollPost(
        val user: String,
        val avatar: Int,
        val question: String,
        val options: List<String>,
        var voteCounts: MutableList<Int> = MutableList(4) { 0 }, // Change 4 if dynamic
        var userVotedIndex: Int? = null
    ) : PostItem()
}
