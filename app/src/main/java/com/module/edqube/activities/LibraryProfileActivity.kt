package com.module.edqube.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.module.edqube.R
import com.module.edqube.adapters.LibraryPagerAdapter
import com.module.edqube.databinding.ActivityLibraryProfileBinding

class LibraryProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* ------------------------------------------------------------------ */
        /* Intent data                                                        */
        /* ------------------------------------------------------------------ */
        val name     = intent.getStringExtra("libraryName")     ?: "Library"
        val distance = intent.getDoubleExtra("libraryDistance", 0.0)
        val imageRes = intent.getIntExtra("libraryImage", R.drawable.libr)

        /* ------------------------------------------------------------------ */
        /* Header population                                                  */
        /* ------------------------------------------------------------------ */
        binding.header.apply {
            ivCover.setImageResource(imageRes)
            ivAvatar.setImageResource(imageRes)
            tvLibraryName.text = name
            tvDistance.text    = getString(R.string.km_format, distance)   // "%,.1f km" in strings.xml
        }

        /* ------------------------------------------------------------------ */
        /* ViewPager + TabLayout                                              */
        /* ------------------------------------------------------------------ */
        binding.viewPager.adapter = LibraryPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = layoutInflater.inflate(R.layout.custom_tab, null) as TextView
            tabView.text = when (position) {
                0 -> "Timing"
                1 -> "Facility"
                2 -> "About"
                3 -> "Gallery"
                else -> ""
            }
            tab.customView = tabView
        }.attach()

    }
}
