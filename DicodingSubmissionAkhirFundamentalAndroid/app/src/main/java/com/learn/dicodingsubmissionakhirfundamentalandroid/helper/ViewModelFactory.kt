package com.learn.dicodingsubmissionakhirfundamentalandroid.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.MainViewModel
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.UsersFavoriteViewModel

class ViewModelFactory private constructor(private val mApplication: Application) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    // Variable untuk menyimpan SettingPreferences
    private var pref: SettingPreferences? = null

    // Method untuk mengatur SettingPreferences
    fun setSettingPreferences(pref: SettingPreferences) {
        this.pref = pref
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersFavoriteViewModel::class.java)) {
            return UsersFavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return pref?.let { MainViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}