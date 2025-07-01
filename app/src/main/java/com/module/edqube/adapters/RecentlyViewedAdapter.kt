package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.databinding.ItemRecentlyViewedBinding
import com.module.edqube.models.RecentlyViewed

// adapter/RecentlyViewedAdapter.kt
class RecentlyViewedAdapter(
    private val items: List<RecentlyViewed>,
    private val onClick: (RecentlyViewed) -> Unit = {}
) : RecyclerView.Adapter<RecentlyViewedAdapter.VH>() {

    inner class VH(val binding: ItemRecentlyViewedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentlyViewed) = with(binding) {
            ivThumbnail.setImageResource(item.imageRes)
            tvTitle.text     = item.title
            tvLocation.text  = item.location
            tvPrice.text     = item.price
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRecentlyViewedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
}
