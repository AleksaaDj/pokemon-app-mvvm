package com.aleksa.samaritanassignment.fragments.community

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksa.samaritanassignment.R
import com.aleksa.samaritanassignment.databinding.FragmentCapturedBinding
import com.aleksa.samaritanassignment.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCommunityBinding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }
}