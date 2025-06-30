package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.databinding.ItemLibraryBinding
import com.module.edqube.models.Library

class LibraryAdapter(
    private val items: List<Library>,
    private val onNavigate: (Library) -> Unit = {}
) : RecyclerView.Adapter<LibraryAdapter.LibraryVH>() {

    inner class LibraryVH(private val binding: ItemLibraryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lib: Library) = with(binding) {
            imgLibrary.setBackgroundResource(lib.imageRes)
            tvName.text = lib.name
            tvDistance.text = String.format("%.1f km", lib.distanceKm)
            tvPhone.text = lib.phone
            tvHours.text = lib.hours

            root.setOnClickListener { onNavigate(lib) } // Click anywhere on card
            btnNavigate.setOnClickListener { onNavigate(lib) }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding  = ItemLibraryBinding.inflate(inflater, parent, false)
        return LibraryVH(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: LibraryVH, position: Int) =
        holder.bind(items[position])
}
