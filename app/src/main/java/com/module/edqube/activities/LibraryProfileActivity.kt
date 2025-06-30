package com.module.edqube.activities

import android.app.Dialog
import android.content.Context
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
import androidx.core.view.children
import com.google.android.material.tabs.TabLayoutMediator
import com.module.edqube.R
import com.module.edqube.adapters.LibraryPagerAdapter
import com.module.edqube.databinding.ActivityLibraryProfileBinding
import java.util.Locale

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

private fun showCalendarDialog(context: Context) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.dialog_calendar)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCanceledOnTouchOutside(true)

    // Get current date
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    // Update month/year display
    val tvMonthYear = dialog.findViewById<TextView>(R.id.tvMonthYear)
    updateMonthYearDisplay(tvMonthYear, currentMonth, currentYear)

    // Setup calendar grid - now passing all required parameters
    val calendarGrid = dialog.findViewById<GridLayout>(R.id.calendarGrid)
    setupCalendarGrid(calendarGrid, currentMonth, currentYear, context)

    // Setup buttons
    dialog.findViewById<Button>(R.id.btnBack).setOnClickListener { dialog.dismiss() }
    dialog.findViewById<Button>(R.id.btnApply).setOnClickListener {
        Toast.makeText(context, "Date selected", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
    }

    dialog.show()
}


private fun updateMonthYearDisplay(textView: TextView, month: Int, year: Int) {
    val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
    }.time)
    textView.text = "$monthName $year"
}

private fun setupCalendarGrid(grid: GridLayout, month: Int, year: Int, context: Context) {
    grid.removeAllViews()

    // Add day headers
    val days = arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    days.forEach { day ->
        val textView = TextView(context).apply {
            text = day
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            textSize = 14f
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4, 4, 4, 4)
            }
        }
        grid.addView(textView)
    }

    // Calculate days in month and first day of week
    val cal = Calendar.getInstance().apply {
        set(Calendar.MONTH, month)
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sunday, 2=Monday, etc.

    // Add empty cells for days before the first day of the month
    for (i in 1 until firstDayOfWeek) {
        val emptyView = TextView(grid.context)
        grid.addView(emptyView)
    }

    // Add day numbers
    for (day in 1..daysInMonth) {
        val textView = TextView(grid.context).apply {
            text = day.toString()
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            textSize = 14f
            setOnClickListener { view ->
                // Highlight selected day
                grid.children.forEach {
                    it.background = null
                    (it as? TextView)?.setTextColor(ContextCompat.getColor(context, android.R.color.black))
                }
                view.setBackgroundResource(R.drawable.circle_background)
                (view as TextView).setTextColor(Color.WHITE)

                // Store selected date
                val selectedDate = "$day/${month + 1}/$year"
            }
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4, 4, 4, 4) // Add margins between cells
            }
        }
        grid.addView(textView)
    }
}