package com.example.cocygo.service.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class Base64ToBitmap {
    companion object{
        fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                null
            }
        }
    }
}