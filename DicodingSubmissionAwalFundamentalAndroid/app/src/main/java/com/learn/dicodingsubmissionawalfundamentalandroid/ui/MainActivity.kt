package com.learn.dicodingsubmissionawalfundamentalandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.dicodingsubmissionawalfundamentalandroid.R
import com.learn.dicodingsubmissionawalfundamentalandroid.data.response.ItemsItem
import com.learn.dicodingsubmissionawalfundamentalandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel.listReview.observe(this) { users ->
            setUsersData(users)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Searching
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                val username = searchView.text.toString().trim()
                mainViewModel.searchUsersByUsername(username)
                searchView.hide()
                true
            }
        }
    }

    private fun setUsersData(users: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUsers.adapter = adapter

        // Menambahkan event click pada item list
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(user: ItemsItem) {
        val detailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.USERNAME, user.login)
        startActivity(detailUserIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}