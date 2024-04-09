package com.learn.dicodingsubmissionakhirfundamentalandroid.ui


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.learn.dicodingsubmissionakhirfundamentalandroid.R
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.local.entity.UsersEntity
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.DetailUserResponse
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.ActivityDetailUserBinding
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.ViewModelFactory
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.adapter.SectionsPagerAdapter
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.DetailUserViewModel
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.UsersFavoriteViewModel

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding

    private val mainViewModel by viewModels<DetailUserViewModel>()
    private lateinit var userFavoriteViewModel: UsersFavoriteViewModel


    companion object {
        const val USERNAME = "username_detail"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    private var user: UsersEntity? = null
    private var isFavorite = false
    private var username = ""
    private var urlAvatar = ""


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

        // Mendapatkan ViewModel dari ViewModelFactory
        userFavoriteViewModel = obtainViewModel(this@DetailUserActivity)


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

            // Memeriksa apakah pengguna favorit atau tidak
            userFavoriteViewModel.getFavoriteUserByUsername(
                intent.getStringExtra(USERNAME).toString()
            )
                .observe(this) {
                    checkFavoriteUser(it)
                }
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

        // Menambahkan dan menghapus user dari daftar favorite database
        binding.addFavorite.setOnClickListener {
            if (isFavorite) {
                userFavoriteViewModel.delete(user as UsersEntity)
                isFavorite = false
                binding.addFavorite.setImageResource(R.drawable.ic_favorite_border)
                Toast.makeText(this, "Di Hapus Dari Daftar Favorit", Toast.LENGTH_SHORT).show()
            } else {
                userFavoriteViewModel.insert(user as UsersEntity)
                isFavorite = true
                binding.addFavorite.setImageResource(R.drawable.ic_favorite)
                Toast.makeText(this, "Di Tambahkan Ke Daftar Favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUserData(userDetail: DetailUserResponse) {
        // Simpan data userDetail ke dalam users database
        user = UsersEntity(userDetail.login.toString(), userDetail.avatarUrl.toString())
        username = userDetail.login.toString()
        urlAvatar = userDetail.avatarUrl.toString()

        Glide.with(this)
            .load(userDetail.avatarUrl)
            .circleCrop()
            .into(binding.ivProfile)
        binding.tvNameDetail.text = userDetail.name
        binding.tvUsernameDetail.text = userDetail.login
        binding.tvNumberFollowers.text = userDetail.followers.toString()
        binding.tvNumberFollowing.text = userDetail.following.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun checkFavoriteUser(favoriteUser: UsersEntity?) {
        if (favoriteUser != null) {
            binding.addFavorite.setImageResource(R.drawable.ic_favorite)
            isFavorite = true
            user = favoriteUser
        } else {
            binding.addFavorite.setImageResource(R.drawable.ic_favorite_border)
            isFavorite = false
            // Tetapkan user ke objek UsersEntity baru
            user = UsersEntity(username, urlAvatar)
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

    private fun obtainViewModel(activity: AppCompatActivity): UsersFavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UsersFavoriteViewModel::class.java)
    }
}