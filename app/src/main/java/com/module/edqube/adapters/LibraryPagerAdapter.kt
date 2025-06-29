package com.module.edqube.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.module.edqube.fragments.*

class LibraryPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 4
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> LibraryTimingFragment()
        1 -> LibraryFacilityFragment()
        2 -> LibraryAboutFragment()
        3 -> LibraryGalleryFragment()
        else -> throw IllegalStateException("Bad index")
    }
}
