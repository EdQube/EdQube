package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter      // ✅ THIS ONE
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.databinding.ItemJobUpdateBinding
import com.module.edqube.models.JobUpdate

class JobsAdapter : ListAdapter<JobUpdate, JobsAdapter.JobVH>(DIFF) {

    inner class JobVH(private val binding: ItemJobUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: JobUpdate) = with(binding) {
            imgLogo.setImageResource(item.logoRes)
            txtTitle.text    = item.title
            txtSubtitle.text = item.subtitle
            txtTime.text     = item.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        JobVH(
            ItemJobUpdateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: JobVH, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JobUpdate>() {
            override fun areItemsTheSame(oldItem: JobUpdate, newItem: JobUpdate) =
                oldItem.title == newItem.title && oldItem.time == newItem.time

            override fun areContentsTheSame(oldItem: JobUpdate, newItem: JobUpdate) =
                oldItem == newItem
        }
    }
}
