package com.aleksa.samaritanassignment.fragments.captured

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksa.samaritanassignment.databinding.FragmentCapturedBinding


class CapturedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCapturedBinding = FragmentCapturedBinding.inflate(inflater, container, false)
        return binding.root
    }

}