package com.aleksa.samaritanassignment.fragments.myteam

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
import com.aleksa.samaritanassignment.adapters.MyTeamAdapter
import com.aleksa.samaritanassignment.databinding.FragmentMyTeamBinding
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.utils.Constants

class MyTeamFragment : Fragment() {

    private lateinit var binding: FragmentMyTeamBinding
    private lateinit var viewModel: MyTeamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTeamBinding.inflate(inflater, container, false)
        setupViewModel()
        val sharedPreference =
            activity?.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME_MAIN, Context.MODE_PRIVATE)
        viewModel.getMyTeam(sharedPreference?.getString(Constants.SHARED_PREFERENCES_TOKEN, "").toString())

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, MyTeamViewModel.ViewModelFactory(
                (activity?.application as PokemonApplication).repository
            )
        ).get(MyTeamViewModel::class.java)
        viewModel.myTeam.observe(viewLifecycleOwner) {
            setupRecyclers(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.animationProgressBar.isVisible = it
        }
    }

    private fun setupRecyclers(myTeam: List<MyTeamItem>) {
        val recyclerMyTeam = binding.recyclerMyteam
        recyclerMyTeam.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerMyTeam.adapter = MyTeamAdapter(myTeam)
    }
}