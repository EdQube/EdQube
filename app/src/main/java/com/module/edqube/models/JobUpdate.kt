package com.module.edqube.models

import androidx.annotation.DrawableRes

data class JobUpdate(
    @DrawableRes val logoRes: Int,
    val title: String,
    val subtitle: String,
    val time: String
)
