package com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.retrofit

import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.DetailUserResponse
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.ItemsItem
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.UsersResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(@Query("q") username: String): Call<UsersResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}