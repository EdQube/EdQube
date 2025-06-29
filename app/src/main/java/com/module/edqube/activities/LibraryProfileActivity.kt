package com.module.edqube.activities

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
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

        val name     = intent.getStringExtra("libraryName")     ?: "Library"
        val distance = intent.getDoubleExtra("libraryDistance", 0.0)
        val imageRes = intent.getIntExtra("libraryImage", R.drawable.libr)

        binding.header.apply {
            ivCover.setImageResource(imageRes)
            ivAvatar.setImageResource(imageRes)
            tvLibraryName.text = name
            tvDistance.text    = getString(R.string.km_format, distance)
        }
        binding.viewPager.adapter = LibraryPagerAdapter(this)



        binding.btnBook.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.popup_select_plan)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()

            dialog.findViewById<ImageView>(R.id.ivClose)?.setOnClickListener { dialog.dismiss() }

            dialog.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnMonthlyBook)?.setOnClickListener {
                // Handle monthly booking
                dialog.dismiss()
            }

            dialog.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDailyBook)?.setOnClickListener {
                // Handle daily booking
                dialog.dismiss()
            }
        }




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
