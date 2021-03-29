package com.udacity

import android.app.Application
import androidx.lifecycle.*

class MainViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel : ViewModel() {

    private val downloadSourceMap = mapOf(
        R.id.rbGlide to "https://github.com/bumptech/glide",
        R.id.rbUdacity to "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",
        R.id.rbRetrofit to "https://github.com/square/retrofit"
    )

    val selectedDownloadButton = MutableLiveData<Int>()

    private var _downloadClicked = MutableLiveData<Boolean>()

    fun onDownloadClicked() {
        _downloadClicked.value = true
    }

    fun handleDownloadClick(owner: LifecycleOwner, handler: () -> Unit) {
        _downloadClicked.observe(owner, Observer {
            it?.let { clicked -> if (clicked) {
                handler()
                _downloadClicked.value = false
            } }
        })
    }

    fun getDownloadUrl() : String {
        return if (selectedDownloadButton.value != null) {
            downloadSourceMap[selectedDownloadButton.value!!] ?: ""
        } else ""
    }

}

