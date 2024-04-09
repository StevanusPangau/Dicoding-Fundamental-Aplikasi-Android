package com.learn.dicodingrestaurantreview.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.learn.dicodingrestaurantreview.R
import com.learn.dicodingrestaurantreview.data.response.CustomerReviewsItem
import com.learn.dicodingrestaurantreview.data.response.PostReviewResponse
import com.learn.dicodingrestaurantreview.data.response.Restaurant
import com.learn.dicodingrestaurantreview.data.response.RestaurantResponse
import com.learn.dicodingrestaurantreview.data.retrofit.ApiConfig
import com.learn.dicodingrestaurantreview.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // code tersebut sudah diubah pakai code ktx yang val mainViewModel by viewModels<MainViewModel>()
//        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.restaurant.observe(this) { restaurant ->
            setRestaurantData(restaurant)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        mainViewModel.listReview.observe(this) { consumerReviews ->
            setReviewData(consumerReviews)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

//        mainViewModel.snackbarText.observe(this) {
//            Snackbar.make(window.decorView.rootView, it, Snackbar.LENGTH_SHORT).show()
//        }
        mainViewModel.snackbarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    window.decorView.rootView,
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnSend.setOnClickListener { view ->
            mainViewModel.postReview(binding.edReview.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setRestaurantData(restaurant: Restaurant) {
        binding.tvTitle.text = restaurant.name
        binding.tvDescription.text = restaurant.description
        Glide.with(this)
//            .load("https://restaurant-api.dicoding.dev/images/large/${restaurant.pictureId}")
            .load("https://post.app/wp-content/uploads/2020/10/Menentukan-Layout-Restoran-atau-Kafe.jpg")
            .into(binding.ivPicture)
    }

    private fun setReviewData(consumerReviews: List<CustomerReviewsItem>) {
        val adapter = ReviewAdapter()
        adapter.submitList(consumerReviews)
        binding.rvReview.adapter = adapter
        binding.edReview.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
