package com.udacity.ui

import android.app.DownloadManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

const val DETAIL_EXTRA_DOWNLOAD_SRC_LABEL = "DownloadSourceLabel"
const val DETAIL_EXTRA_STATUS = "status"

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        val label = intent.getStringExtra(DETAIL_EXTRA_DOWNLOAD_SRC_LABEL)
        val status = intent.getIntExtra(DETAIL_EXTRA_STATUS, DownloadManager.STATUS_FAILED)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

}
