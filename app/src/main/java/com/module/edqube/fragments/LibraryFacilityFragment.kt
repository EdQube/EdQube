package com.module.edqube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R

class LibraryFacilityFragment : Fragment() {

    private val facilities = listOf(
        "Air Condition", "Wi-Fi", "Study Space", "Printing",
        "Changing Station", "Group Study Room", "Refreshment Area"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_library_facility, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFacility)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = FacilityAdapter(facilities)
    }

    class FacilityAdapter(private val items: List<String>) : RecyclerView.Adapter<FacilityAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_facility, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvFacility.text = items[position]
        }

        override fun getItemCount() = items.size

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvFacility: TextView = view.findViewById(R.id.tvFacility)
        }
    }
}