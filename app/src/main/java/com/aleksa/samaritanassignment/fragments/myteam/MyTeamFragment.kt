package com.aleksa.samaritanassignment.fragments.myteam

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleksa.samaritanassignment.adapters.MyTeamAdapter
import com.aleksa.samaritanassignment.databinding.FragmentMyTeamBinding
import com.aleksa.samaritanassignment.models.MyTeamItem
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService

class MyTeamFragment : Fragment() {

    lateinit var binding: FragmentMyTeamBinding
    lateinit var viewModel: MyTeamViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTeamBinding.inflate(inflater, container, false)
        setupViewModel()
        val sharedPreference =
            activity?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        viewModel.getMyTeam(sharedPreference?.getString("token", "").toString())

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, MyTeamViewModel.ViewModelFactory(
                MainRepository(retrofitService)
            )
        ).get(MyTeamViewModel::class.java)
        viewModel.myTeam.observe(viewLifecycleOwner) {
            setupRecyclers(it)
        }
    }

    private fun setupRecyclers(myTeam: List<MyTeamItem>) {
        val recyclerMyTeam = binding.recyclerMyteam
        recyclerMyTeam.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerMyTeam.adapter = MyTeamAdapter(myTeam)
    }
}