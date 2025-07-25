package com.module.edqube

object Utils {
    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        if (diff < 0) return "In the future"

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            days < 30 -> "$days day${if (days > 1) "s" else ""} ago"
            months < 12 -> "$months month${if (months > 1) "s" else ""} ago"
            else -> "$years year${if (years > 1) "s" else ""} ago"
        }
    }

}