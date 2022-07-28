package com.udacity.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

object Notifications {
    private const val NOTIFICATION_ID = 0
    private const val CHANNEL_ID = "channelId"

    fun cancelNotification(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    fun showNotification(context: Context, urls: Urls) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle(getTitle(context, urls))
            .setContentText(getText(context, urls))
            .setAutoCancel(true)
            .addAction(getAction(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getTitle(context: Context, urls: Urls) = when (urls) {
        Urls.URL1 -> context.getString(R.string.notification_title1)
        Urls.URL2 -> context.getString(R.string.notification_title2)
        Urls.URL3 -> context.getString(R.string.notification_title3)
        else -> context.getString(R.string.notification_title1)
    }

    private fun getText(context: Context, urls: Urls) = when (urls) {
        Urls.URL1 -> context.getString(R.string.notification_description1)
        Urls.URL2 -> context.getString(R.string.notification_description2)
        Urls.URL3 -> context.getString(R.string.notification_description3)
        else -> context.getString(R.string.notification_description1)
    }

    private fun getAction(context: Context) = NotificationCompat.Action.Builder(
        R.drawable.ic_download,
        context.getString(R.string.notification_button),
        getActionPendingIntent(context)
    ).build()

    private fun getActionPendingIntent(context: Context) = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        Intent(context, DetailActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.channel_name)
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = context.getString(R.string.channel_description)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}