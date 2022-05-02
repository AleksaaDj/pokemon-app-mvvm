package com.aleksa.samaritanassignment.fragments.community

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleksa.samaritanassignment.adapters.FoesAdapter
import com.aleksa.samaritanassignment.adapters.FriendsAdapter
import com.aleksa.samaritanassignment.databinding.FragmentCommunityBinding
import com.aleksa.samaritanassignment.models.Community
import com.aleksa.samaritanassignment.models.Friends
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    lateinit var viewModel: CommunityViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        setupViewModel()
        val sharedPreference =
            activity?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        viewModel.getCommunity(sharedPreference?.getString("token", "").toString())
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, CommunityViewModel.ViewModelFactory(
                MainRepository(retrofitService)
            )
        ).get(CommunityViewModel::class.java)
        viewModel.community.observe(viewLifecycleOwner) {
            setupRecyclers(it)
        }
    }

    private fun setupRecyclers(community: Community) {
        //creating recycler val
        val recyclerFriends = binding.recyclerFriends
        val recyclerFoes = binding.recyclerFoes

        //setting recycler to horizontal scroll
        recyclerFriends.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerFoes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //setting adapter to recycler
        recyclerFriends.adapter = FriendsAdapter(community.friends)
        recyclerFoes.adapter = FoesAdapter(community.foes)
    }
}