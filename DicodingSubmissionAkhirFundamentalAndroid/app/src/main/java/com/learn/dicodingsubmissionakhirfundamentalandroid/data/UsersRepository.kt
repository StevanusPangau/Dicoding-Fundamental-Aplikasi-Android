package com.learn.dicodingsubmissionakhirfundamentalandroid.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.room.UsersDao
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.room.UsersDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UsersRepository(application: Application) {
    private val mUsersDao: UsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UsersDatabase.getDatabase(application)
        mUsersDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<UsersEntity>> = mUsersDao.getAllNews()

    fun getUserById(username: String): LiveData<UsersEntity> {
        return mUsersDao.getFavoriteUserByUsername(username)
    }

    fun insert(user: UsersEntity) {
        executorService.execute { mUsersDao.insert(user) }
    }

    fun delete(user: UsersEntity) {
        executorService.execute { mUsersDao.delete(user) }
    }

}