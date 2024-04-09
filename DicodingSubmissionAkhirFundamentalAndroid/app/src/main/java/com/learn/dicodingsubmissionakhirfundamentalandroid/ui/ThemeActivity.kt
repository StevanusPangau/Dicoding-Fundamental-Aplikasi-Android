package com.learn.dicodingsubmissionakhirfundamentalandroid.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.learn.dicodingsubmissionakhirfundamentalandroid.R
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.ActivityThemeBinding
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.SettingPreferences
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.ViewModelFactory
import com.learn.dicodingsubmissionakhirfundamentalandroid.helper.dataStore
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.MainViewModel

class ThemeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val switchTheme = binding.switchTheme
        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(application)
        viewModelFactory.setSettingPreferences(pref)

        val mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        // Mengaktifkan tombol kembali di AppBar
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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