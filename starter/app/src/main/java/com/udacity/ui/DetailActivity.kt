package com.udacity.ui

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityDetailBinding

const val DETAIL_EXTRA_DOWNLOAD_SRC_LABEL = "DownloadSourceLabel"
const val DETAIL_EXTRA_STATUS = "status"


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        val label = intent.getStringExtra(DETAIL_EXTRA_DOWNLOAD_SRC_LABEL)
        val status = intent.getIntExtra(DETAIL_EXTRA_STATUS, DownloadManager.STATUS_FAILED)

        val modelFactory = DetailViewModelFactory(this)
        model = ViewModelProvider(this, modelFactory).get(DetailViewModel::class.java).apply {
            setLabel(label ?: "")
            setStatus(status)
        }
        binding.model = model

        model.handleOkClick(this) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

}
