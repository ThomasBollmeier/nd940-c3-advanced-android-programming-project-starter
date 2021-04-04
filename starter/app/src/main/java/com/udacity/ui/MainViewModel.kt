package com.udacity.ui

import androidx.lifecycle.*
import com.udacity.R

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
        R.id.rbGlide to Pair("https://github.com/bumptech/glide", R.string.glide_option),
        R.id.rbUdacity to Pair(
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter",
            R.string.udacity_option
        ),
        R.id.rbRetrofit to Pair("https://github.com/square/retrofit", R.string.retrofit2_option)
    )

    val selectedDownloadButton = MutableLiveData<Int>()

    private var _downloadClicked = MutableLiveData<Boolean>()

    fun onDownloadClicked() {
        _downloadClicked.value = true
    }

    fun handleDownloadClick(owner: LifecycleOwner, handler: (url: String) -> Unit) {
        _downloadClicked.observe(owner, Observer {
            it?.let { clicked ->
                if (clicked) {
                    val url = getDownloadUrl()
                    handler(url)
                    _downloadClicked.value = false
                }
            }
        })
    }

    fun getDownloadSourceLabelResId(url: String): Int? {
        for ((_url, resId) in downloadSourceMap.values) {
            if (_url == url) {
                return resId
            }
        }
        return null
    }

    private fun getDownloadUrl(): String {
        return if (selectedDownloadButton.value != null) {
            downloadSourceMap[selectedDownloadButton.value!!]?.first ?: ""
        } else ""
    }

}

