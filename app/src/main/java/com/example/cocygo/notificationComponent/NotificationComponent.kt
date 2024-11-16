package com.example.cocygo.notificationComponent

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationComponent(private val context: Context) {

    private val defaultChannelId = "high_priority_channel"
    private val defaultChannelName = "High Priority Channel"

    // Init
    init {
        createNotificationChannel()
    }

    // Create the channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH //heads-up
            val channel = NotificationChannel(defaultChannelId, defaultChannelName, importance).apply {
                description = "This channel is used for Booking notifications"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Show notifications
    fun showNotification(title: String, message: String, notificationId: Int = 1) {
        val notification = NotificationCompat.Builder(context, defaultChannelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // icon
            .setContentTitle(title) // title
            .setContentText(message) // content
            .setPriority(NotificationCompat.PRIORITY_HIGH) //  Heads-Up
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // category
            .build()

        // check permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("Notification permission not granted")
        } else {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
            println("Notification permission granted")
        }
    }
}
