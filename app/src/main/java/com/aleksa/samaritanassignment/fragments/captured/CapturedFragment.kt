package com.aleksa.samaritanassignment.fragments.captured

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleksa.samaritanassignment.adapters.CapturedAdapter
import com.aleksa.samaritanassignment.adapters.MyTeamAdapter
import com.aleksa.samaritanassignment.databinding.FragmentCapturedBinding
import com.aleksa.samaritanassignment.fragments.community.CommunityViewModel
import com.aleksa.samaritanassignment.models.Captured
import com.aleksa.samaritanassignment.models.CapturedItem
import com.aleksa.samaritanassignment.models.Pokemon
import com.aleksa.samaritanassignment.network.MainRepository
import com.aleksa.samaritanassignment.network.RetrofitService


class CapturedFragment : Fragment() {
    lateinit var binding: FragmentCapturedBinding
    private lateinit var viewModel: CapturedViewModel
    private val retrofitService = RetrofitService.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCapturedBinding.inflate(inflater, container, false)
        setupViewModel()
        val sharedPreference =
            activity?.getSharedPreferences("SAMARITAN_PREFERENCE", Context.MODE_PRIVATE)
        viewModel.getCaptured(sharedPreference?.getString("token", "").toString())

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, CapturedViewModel.ViewModelFactory(
                MainRepository(retrofitService)
            )
        ).get(CapturedViewModel::class.java)
        viewModel.captured.observe(viewLifecycleOwner) {
            setupRecyclers(it)
        }
    }

    private fun setupRecyclers(capturedItems: List<CapturedItem>) {
        //creating recycler val
        val recyclerCaptured = binding.recyclerCaptured

        //setting recycler to horizontal scroll
        recyclerCaptured.layoutManager = GridLayoutManager(context,3)

        //setting adapter to recycler
        recyclerCaptured.adapter = CapturedAdapter(capturedItems)
    }

}