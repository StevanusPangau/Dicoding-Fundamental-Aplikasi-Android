package com.learn.dicodingsubmissionakhirfundamentalandroid.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.dicodingsubmissionakhirfundamentalandroid.data.remote.response.ItemsItem
import com.learn.dicodingsubmissionakhirfundamentalandroid.databinding.FragmentFollowersFollowingBinding
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.adapter.UserAdapter
import com.learn.dicodingsubmissionakhirfundamentalandroid.ui.model.DetailUserViewModel

class FollowersFollowingFragment : Fragment(), UserAdapter.OnItemClickCallback {
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

        val adapter = UserAdapter()
        adapter.setOnItemClickCallback(this)
        binding.rvFollow.adapter = adapter
    }

    private fun setUsersData(users: List<ItemsItem>) {
        val adapter = binding.rvFollow.adapter as? UserAdapter
        adapter?.submitList(users)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClicked(data: ItemsItem) {
        showSelectedHero(data)
    }

    private fun showSelectedHero(user: ItemsItem) {
        val detailUserIntent = Intent(requireContext(), DetailUserActivity::class.java)
        detailUserIntent.putExtra(DetailUserActivity.USERNAME, user.login)
        startActivity(detailUserIntent)
    }
}