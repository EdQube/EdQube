package com.module.edqube.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.module.edqube.fragments.ProfilePostFragment
import com.module.edqube.fragments.ProfilePostLikeFragment
import com.module.edqube.fragments.ProfileMediaFragment

class ProfileAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> ProfilePostFragment()
        1 -> ProfilePostLikeFragment()
        2 -> ProfileMediaFragment()
        else -> throw IllegalStateException("Invalid position $position")
    }
}
