package com.module.edqube.models

data class Library(
    val name: String,
    val distanceKm: Double,
    val phone: String,
    val hours: String,
    val imageRes: Int           // drawable resource id
)
