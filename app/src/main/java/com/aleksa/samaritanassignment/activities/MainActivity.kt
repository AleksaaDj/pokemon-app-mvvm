package com.aleksa.samaritanassignment.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aleksa.samaritanassignment.adapters.FragmentPagerAdapter
import com.aleksa.samaritanassignment.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics

val tabsNameArray = arrayOf(
    "Explore",
    "Community",
    "My Team",
    "Captured"
)

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()
        setContentView(view)

        @SuppressLint("MissingPermission")
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsNameArray[position]
        }.attach()
    }
}