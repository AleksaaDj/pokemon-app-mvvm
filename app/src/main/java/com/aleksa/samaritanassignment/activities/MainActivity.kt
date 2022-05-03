package com.aleksa.samaritanassignment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aleksa.samaritanassignment.adapters.FragmentPagerAdapter
import com.aleksa.samaritanassignment.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

val tabsNameArray = arrayOf(
    "Explore",
    "Community",
    "My Team",
    "Captured"
)

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()
        setContentView(view)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = FragmentPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabsNameArray[position]
        }.attach()
    }
}