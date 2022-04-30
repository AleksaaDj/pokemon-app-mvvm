package com.aleksa.samaritanassignment.fragments.myteam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksa.samaritanassignment.databinding.FragmentMyTeamBinding

class MyTeamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMyTeamBinding = FragmentMyTeamBinding.inflate(inflater, container, false)
        return binding.root
    }
}