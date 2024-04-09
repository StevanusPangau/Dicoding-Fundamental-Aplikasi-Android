package com.learn.dicodingsubmissionawalfundamentalandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.dicodingsubmissionawalfundamentalandroid.data.response.ItemsItem
import com.learn.dicodingsubmissionawalfundamentalandroid.databinding.FragmentFollowersFollowingBinding

class FollowersFollowingFragment : Fragment() {
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<DetailUserViewModel>()

    private var position: Int = 0
    private var username: String = ""

    private var isDataLoaded = false

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "section_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        mainViewModel.listFollowers.observe(viewLifecycleOwner) { users ->
            setUsersData(users)
            isDataLoaded = true
        }

        mainViewModel.listFollowing.observe(viewLifecycleOwner) { users ->
            setUsersData(users)
            isDataLoaded = true
        }

//        mainViewModel.isLoading.observe(viewLifecycleOwner) {
//            showLoading(it)
//        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isDataLoaded) {
                showLoading(isLoading)
            }
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        if (position == 1) {
            mainViewModel.findFollowers(username)
        } else {
            mainViewModel.findFollowing(username)
        }
    }

    private fun setUsersData(users: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}