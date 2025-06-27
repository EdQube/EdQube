package com.module.edqube

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.module.edqube.adapters.ViewPagerAdapter
import com.module.edqube.databinding.ActivityMainBinding   // ← auto-generated
import com.module.edqube.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding      // single binding ref

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate once, no findViewById needed
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewPager2 + adapter
        val pagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.apply {
            adapter = pagerAdapter
            // explicit but optional – swiping is true by default
            isUserInputEnabled = true
        }

        // Tab text & custom font
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = layoutInflater.inflate(R.layout.custom_tab, null) as TextView
            tabView.text = when (position) {
                0 -> "Libraries"
                1 -> "Posts"
                else -> "Coachings"
            }
            tabView.typeface = resources.getFont(R.font.mregular)
            tab.customView = tabView
        }.attach()
    }
}
