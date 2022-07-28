package com.udacity.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import com.udacity.R

enum class Urls(val url: String) {
    EMPTY(""),
    URL1("https://github.com/bumptech/glide/archive/master.zip"),
    URL2("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
    URL3("https://github.com/square/retrofit/archive/master.zip")
}

object Downloader {
    var downloadID: Long = 0
    var downloadUrl = Urls.EMPTY

    fun download(context: Context, urls: Urls) {
        downloadUrl = urls
        val request = DownloadManager.Request(Uri.parse(urls.url))
            .setTitle(context.getString(R.string.app_name))
            .setDescription(context.getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = ContextCompat.getSystemService(context, DownloadManager::class.java)
        downloadID = downloadManager?.enqueue(request) ?: 0
    }

}