package com.example.cocygo.homeFragment.adapter

import android.media.AudioDescriptor
import android.media.Rating
import kotlin.time.Duration

data class Service(
    val id:String,
    val duration: Int,
    val image: String,
    val rating: Float,
    val serviceName: String,
    val state: String,
    val stylist: String,
    val description: String,
    val likeStatus:Boolean
)
