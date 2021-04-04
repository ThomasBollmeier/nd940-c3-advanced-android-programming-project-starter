package com.udacity.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.udacity.R
import com.udacity.core.Downloader
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloader: Downloader

    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        model = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
        mainBinding.model = model

        downloader = Downloader(this) { id, uri, status ->
            if (id == downloadID) {
                sendNotification(uri, status)
            }
        }

        model.handleDownloadClick(this) { url ->
            if (url.isNotEmpty()) {
                downloadID = downloader.download(
                    url,
                    getString(R.string.app_name),
                    getString(R.string.app_description)
                )
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.no_selection_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        setContentView(mainBinding.root)
        setSupportActionBar(mainBinding.toolbar)

        createNotificationChannel()

    }

    private fun createNotificationChannel() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name_downloader)
            val descriptionText = getString(R.string.channel_description_downloader)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendNotification(uri: String, status: Int) {

        val notificationId = 1

        val detailIntent = Intent(this, DetailActivity::class.java).apply {

            val resId = model.getDownloadSourceLabelResId(uri)
            val label = if (resId != null) getString(resId) else ""

            putExtra(DETAIL_EXTRA_DOWNLOAD_SRC_LABEL, label)
            putExtra(DETAIL_EXTRA_STATUS, status)
        }

        val pendingDetailIntent =
            PendingIntent.getActivity(this, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                R.drawable.ic_launcher_foreground,
                getString(R.string.notification_button),
                pendingDetailIntent
            )
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }

    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
