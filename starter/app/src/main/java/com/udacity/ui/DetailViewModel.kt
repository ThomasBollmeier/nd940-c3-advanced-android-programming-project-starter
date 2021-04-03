package com.udacity.ui

import android.app.DownloadManager
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.MainViewModel
import com.udacity.R

class DetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return DetailViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class DetailViewModel(private val context: Context) : ViewModel() {

    private var _label = MutableLiveData<String>()

    val label: LiveData<String>
        get() = _label

    private var _statusDescription = MutableLiveData<String>()
    val statusDescription: LiveData<String>
        get() = _statusDescription

    fun setLabel(label: String) {
        _label.value = label
    }

    fun setStatus(status: Int) {
        _statusDescription.value = when (status) {
            DownloadManager.STATUS_FAILED -> context.getString(R.string.status_failed)
            DownloadManager.STATUS_PAUSED -> context.getString(R.string.status_paused)
            DownloadManager.STATUS_PENDING -> context.getString(R.string.status_pending)
            DownloadManager.STATUS_RUNNING -> context.getString(R.string.status_running)
            DownloadManager.STATUS_SUCCESSFUL -> context.getString(R.string.status_successful)
            else -> "Unknown"
        }
    }
}