package com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "username")
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null,
) : Parcelable