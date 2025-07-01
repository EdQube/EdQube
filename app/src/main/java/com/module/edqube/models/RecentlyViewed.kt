package com.module.edqube.models

// model/RecentlyViewed.kt
data class RecentlyViewed(
    val imageRes: Int,   // drawable ID
    val title: String,
    val location: String,
    val price: String
)