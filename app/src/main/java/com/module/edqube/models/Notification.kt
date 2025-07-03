package com.module.edqube.models

data class Notification(
    val profileImageRes: Int,
    val text: String,
    val time: String,
    val isUnread: Boolean = false
)
