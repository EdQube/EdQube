package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.databinding.ItemRecentSearchBinding
import com.module.edqube.models.RecentSearch

// adapter/RecentSearchAdapter.kt
class RecentSearchAdapter(
    private val items: List<RecentSearch>,
    private val onClick: (RecentSearch) -> Unit = {}
) : RecyclerView.Adapter<RecentSearchAdapter.VH>() {

    inner class VH(val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecentSearch) = with(binding) {
            tvTitle.text = item.title
            tvSub.text   = item.subtitle
            root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRecentSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
}
