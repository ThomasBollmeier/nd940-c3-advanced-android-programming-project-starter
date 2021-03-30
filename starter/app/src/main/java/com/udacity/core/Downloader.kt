package com.udacity.core

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri

class Downloader(
    private val context: Context,
    private val onDownloadComplete: (id: Long) -> Unit
) : BroadcastReceiver() {

    init {
        context.registerReceiver(this, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun download(
        url: String,
        title: String,
        description: String
    ): Long {

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription(description)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        return downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
        onDownloadComplete(id)
    }

}