package com.learn.dicodingsubmissionawalfundamentalandroid.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.learn.dicodingsubmissionawalfundamentalandroid.R
import com.learn.dicodingsubmissionawalfundamentalandroid.data.response.DetailUserResponse
import com.learn.dicodingsubmissionawalfundamentalandroid.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val mainViewModel by viewModels<DetailUserViewModel>()

    companion object {
        const val USERNAME = "username_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Memanggil data user dari ViewModel
        mainViewModel.userDetail.observe(this) { userDetail ->
            setUserData(userDetail)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        // Memanggil findDetailUser hanya saat activity dibuat pertama kali
        if (savedInstanceState == null) {
            mainViewModel.findDetailUser(intent.getStringExtra(USERNAME).toString())
        }

        // Mengatur ViewPager dan TabLayout
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        sectionsPagerAdapter.username = intent.getStringExtra(USERNAME).toString()
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // Mengaktifkan tombol kembali di AppBar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profile ${intent.getStringExtra(USERNAME).toString()}"
    }


    private fun setUserData(user: DetailUserResponse) {
        Glide.with(this)
            .load(user.avatarUrl)
            .circleCrop()
            .into(binding.ivProfile)
        binding.tvNameDetail.text = user.name
        binding.tvUsernameDetail.text = user.login
        binding.tvNumberFollowers.text = user.followers.toString()
        binding.tvNumberFollowing.text = user.following.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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