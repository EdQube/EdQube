package com.module.edqube

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.module.edqube.databinding.ActivityMainBinding
import com.module.edqube.fragments.CreatePostFragment
import com.module.edqube.fragments.JobsFragment
import com.module.edqube.fragments.ProfileFragment
import com.module.edqube.fragments.SearchFragment
import com.module.edqube.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var activeFragment: Fragment? = null          // to avoid re-creating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // preload all fragments once (optional but smoothest)
        val home     = HomeFragment()
        val search   = SearchFragment()
        val post     = CreatePostFragment()
        val jobs     = JobsFragment()
        val profile  = ProfileFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profile,  "profile").hide(profile)
            .add(R.id.fragment_container, jobs,     "jobs").hide(jobs)
            .add(R.id.fragment_container, post,     "post").hide(post)
            .add(R.id.fragment_container, search,   "search").hide(search)
            .add(R.id.fragment_container, home,     "home")           // shown by default
            .commit()
        activeFragment = home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val next = when (item.itemId) {
                R.id.menu_search  -> search
                R.id.menu_post    -> post
                R.id.menu_jobs    -> jobs
                R.id.menu_profile -> profile
                else              -> home
            }
            if (next != activeFragment) {
                supportFragmentManager.beginTransaction()
                    .hide(activeFragment!!)
                    .show(next)
                    .commit()
                activeFragment = next
            }
            true
        }
    }
}
