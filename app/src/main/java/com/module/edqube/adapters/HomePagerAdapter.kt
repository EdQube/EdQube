package com.module.edqube.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.module.edqube.fragments.CoachingFragment
import com.module.edqube.fragments.LibraryFragment
import com.module.edqube.fragments.PostFragment

class HomePagerAdapter(parent: Fragment) : FragmentStateAdapter(parent) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> LibraryFragment()
        1 -> PostFragment()
        else -> CoachingFragment()
    }
}
