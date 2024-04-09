package com.learn.dicodingsubmissionakhirfundamentalandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.dicodingsubmissionakhirfundamentalandroid.R
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.ActivityFavoriteBinding
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.ViewModelFactory
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.adapter.UserFavoriteAdapter
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.UsersFavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userFavoriteViewModel: UsersFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userFavoriteViewModel = obtainViewModel(this@FavoriteActivity)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)

        userFavoriteViewModel.getAllUsers().observe(this) { favoriteUsers ->
            setUsersData(favoriteUsers)
        }

        // Mengaktifkan tombol kembali di AppBar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUsersData(users: List<UsersEntity>) {
        val adapter = UserFavoriteAdapter()
        adapter.submitList(users)
        binding.rvFavorite.adapter = adapter

        // Menambahkan event click pada item list
        adapter.setOnItemClickCallback(object : UserFavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersEntity) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(user: UsersEntity) {
        val detailUserIntent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.USERNAME, user.username)
        startActivity(detailUserIntent)
    }

    private fun obtainViewModel(activity: AppCompatActivity): UsersFavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UsersFavoriteViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
