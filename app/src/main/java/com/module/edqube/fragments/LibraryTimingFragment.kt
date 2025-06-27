package com.module.edqube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R

class LibraryTimingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val timings = listOf(
        Timing("Monday", true, "9:00", "22:00"),
        Timing("Tuesday", true, "9:00", "22:00"),
        Timing("Wednesday", true, "9:00", "22:00"),
        Timing("Friday", true, "9:00", "22:00"),
        Timing("Saturday", true, "9:00", "22:00"),
        Timing("Sunday", false, "9:00", "22:00")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_library_timing, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rvTiming)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TimingAdapter(timings)
    }
}

data class Timing(val day: String, val isOpen: Boolean, val openTime: String, val closeTime: String)

class TimingAdapter(private val items: List<Timing>) : RecyclerView.Adapter<TimingAdapter.TimingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TimingViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_timing, parent, false)
    )

    override fun onBindViewHolder(holder: TimingViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    inner class TimingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDay       = view.findViewById<TextView>(R.id.tvDay)
        private val tvStatus    = view.findViewById<TextView>(R.id.tvStatus)
        private val tvOpenTime  = view.findViewById<TextView>(R.id.tvOpenTime)
        private val tvCloseTime = view.findViewById<TextView>(R.id.tvCloseTime)

        fun bind(item: Timing) {
            tvDay.text = item.day

            // status colour
            val colorRes = if (item.isOpen) R.color.secondary else R.color.red
            tvStatus.text      = if (item.isOpen) "Open" else "Close"
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.context, colorRes))

            // enable / disable chips
            val enabled = item.isOpen
            tvOpenTime.isEnabled  = enabled
            tvCloseTime.isEnabled = enabled

            tvOpenTime.text  = item.openTime
            tvCloseTime.text = item.closeTime
        }
    }

}