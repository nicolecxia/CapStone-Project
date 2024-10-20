package com.example.capstonefragment.model

import android.media.AudioDescriptor
import android.media.Rating
import kotlin.time.Duration

data class Service(
    val duration: Int,
    val image: String,
    val rating: Float,
    val serviceName: String,
    val state: String,
    val stylist: String,
    val description: String
)
