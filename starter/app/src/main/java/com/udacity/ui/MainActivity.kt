package com.udacity.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.udacity.MainViewModel
import com.udacity.MainViewModelFactory
import com.udacity.R
import com.udacity.core.Downloader
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloader: Downloader

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)

        model = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
        mainBinding.model = model

        downloader = Downloader(this) { id ->
            if (id == downloadID) {
                sendNotification()
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

    private fun sendNotification() {

        val notificationId = 1

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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
