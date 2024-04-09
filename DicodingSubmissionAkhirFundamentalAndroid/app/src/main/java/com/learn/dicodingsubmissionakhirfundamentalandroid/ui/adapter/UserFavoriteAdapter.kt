package com.learn.dicodingsubmissionakhirfundamentalandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.ItemRowUserBinding

class UserFavoriteAdapter : ListAdapter<UsersEntity, UserFavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFavoriteAdapter.MyViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserFavoriteAdapter.MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(review)
        }
    }

    class MyViewHolder(val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UsersEntity) {
            binding.tvUsername.text = user.username
            Glide.with(this.itemView.context)
                .load(user.avatarUrl)
                .into(binding.ivPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsersEntity>() {
            override fun areItemsTheSame(
                oldItem: UsersEntity,
                newItem: UsersEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UsersEntity,
                newItem: UsersEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UsersEntity)
    }
}