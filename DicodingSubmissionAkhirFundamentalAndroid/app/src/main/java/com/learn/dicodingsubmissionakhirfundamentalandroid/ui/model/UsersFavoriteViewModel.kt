package com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.UsersRepository
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity

class UsersFavoriteViewModel(application: Application) : ViewModel() {
    private val mUsersRepository: UsersRepository = UsersRepository(application)
    fun insert(user: UsersEntity) {
        mUsersRepository.insert(user)
    }

    fun delete(user: UsersEntity) {
        mUsersRepository.delete(user)
    }

    // Fungsi untuk mendapatkan pengguna favorit berdasarkan username
    fun getFavoriteUserByUsername(username: String): LiveData<UsersEntity> {
        return mUsersRepository.getUserById(username)
    }

    // Fungsi untuk mendapatkan semua pengguna favorit
    fun getAllUsers(): LiveData<List<UsersEntity>> {
        return mUsersRepository.getAllUsers()
    }
}