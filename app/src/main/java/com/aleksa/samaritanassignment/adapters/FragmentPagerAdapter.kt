package com.aleksa.samaritanassignment.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aleksa.samaritanassignment.fragments.captured.CapturedFragment
import com.aleksa.samaritanassignment.fragments.community.CommunityFragment
import com.aleksa.samaritanassignment.fragments.explore.ExploreFragment
import com.aleksa.samaritanassignment.fragments.myteam.MyTeamFragment


private const val NUM_TABS = 4

class FragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ExploreFragment()
            1 -> return CommunityFragment()
            2 -> return MyTeamFragment()
            3 -> return CapturedFragment()
        }
        return ExploreFragment()
    }
}