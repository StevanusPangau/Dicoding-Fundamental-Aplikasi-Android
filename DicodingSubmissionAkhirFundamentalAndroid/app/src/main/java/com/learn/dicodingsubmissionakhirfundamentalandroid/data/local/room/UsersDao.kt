package com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users ORDER BY username ASC")
    fun getAllNews(): LiveData<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UsersEntity)

    @Delete
    fun delete(user: UsersEntity)

    @Query("SELECT * FROM users WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<UsersEntity>
}