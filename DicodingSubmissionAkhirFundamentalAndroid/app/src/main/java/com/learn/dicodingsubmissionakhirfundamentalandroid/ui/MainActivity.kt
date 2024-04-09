package com.learn.dicodingsubmissionakhirfundamentalandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.dicodingsubmissionakhirfundamentalandroid.R
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.ItemsItem
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.ActivityMainBinding
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.SettingPreferences
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.ViewModelFactory
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.dataStore
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.adapter.UserAdapter
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        // Fungsi ini untuk menganti tema
        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(application)
        viewModelFactory.setSettingPreferences(pref)
        val mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            invalidateOptionsMenu()
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

        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_theme -> {
                    val intent = Intent(this, ThemeActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
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