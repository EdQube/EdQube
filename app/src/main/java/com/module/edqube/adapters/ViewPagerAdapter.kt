package com.module.edqube.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.module.edqube.fragments.CoachingFragment
import com.module.edqube.fragments.LibraryFragment
import com.module.edqube.fragments.PostFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LibraryFragment()
            1 -> PostFragment()
            2 -> CoachingFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
