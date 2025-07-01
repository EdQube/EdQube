package com.module.edqube.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.children
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import com.module.edqube.R
import com.module.edqube.adapters.LibraryPagerAdapter
import com.module.edqube.databinding.ActivityLibraryProfileBinding
import java.util.Date
import java.util.Locale

class LibraryProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, true)


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
                dialog.dismiss()
                showCalendarDialog(this@LibraryProfileActivity)  // Replace YourActivityName with your actual activity class name
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

fun showCalendarDialog(context: Context) {
    val picker = MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Select Date")
        // 👇 forces the compact‑dialog variant + hooks in our colours
        .setTheme(R.style.ThemeOverlay_App_DatePicker)   // dots → underscores
        .build()

    picker.addOnPositiveButtonClickListener { range ->
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val msg  = "${sdf.format(Date(range.first!!))} → ${sdf.format(Date(range.second!!))}"
        val i = Intent(context, BookingDetailsActivity::class.java)
        context.startActivity(i)
        Toast.makeText(context, "Selected: $msg", Toast.LENGTH_SHORT).show()
    }

    (context as AppCompatActivity)
        .supportFragmentManager
        .also { picker.show(it, "DATE_PICKER") }
}
