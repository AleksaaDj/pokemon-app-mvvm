package com.aleksa.samaritanassignment.fragments.community

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleksa.samaritanassignment.PokemonApplication
import com.aleksa.samaritanassignment.adapters.FoesAdapter
import com.aleksa.samaritanassignment.adapters.FriendsAdapter
import com.aleksa.samaritanassignment.databinding.FragmentCommunityBinding
import com.aleksa.samaritanassignment.models.Community
import com.aleksa.samaritanassignment.utils.Constants

class CommunityFragment : Fragment() {
    private lateinit var binding: FragmentCommunityBinding
    private lateinit var viewModel: CommunityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityBinding.inflate(inflater, container, false)
        setupViewModel()
        val sharedPreference =
            activity?.getSharedPreferences(
                Constants.SHARED_PREFERENCES_NAME_MAIN,
                Context.MODE_PRIVATE
            )
        viewModel.getCommunity(
            sharedPreference?.getString(Constants.SHARED_PREFERENCES_TOKEN, "").toString()
        )
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, CommunityViewModel.ViewModelFactory(
                (activity?.application as PokemonApplication).repository
            )
        ).get(CommunityViewModel::class.java)
        viewModel.community.observe(viewLifecycleOwner) {
            setupRecyclers(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.animationProgressBar.isVisible = it
            setupProgressBar(it)
        }
    }

    private fun setupRecyclers(community: Community) {
        val recyclerFriends = binding.recyclerFriends
        val recyclerFoes = binding.recyclerFoes
        recyclerFriends.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerFoes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerFriends.adapter = FriendsAdapter(community.friends)
        recyclerFoes.adapter = FoesAdapter(community.foes)
    }

    private fun setupProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.animationProgressBar.isVisible = true
            binding.linearLayoutFriendsFoes.isVisible = false
        } else {
            binding.animationProgressBar.isVisible = false
            binding.linearLayoutFriendsFoes.isVisible = true
        }
    }
}