package com.module.edqube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.databinding.ItemCoachingBinding
import com.module.edqube.models.Coaching

class CoachingAdapter(
    private val items: List<Coaching>,
    private val onNavigate: (Coaching) -> Unit = {}
) : RecyclerView.Adapter<CoachingAdapter.CoachingVH>() {

    inner class CoachingVH(private val binding: ItemCoachingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coaching: Coaching) = with(binding) {
            imgCoaching.setImageResource(coaching.imageRes)
            tvName.text = coaching.name
            tvDistance.text = String.format("%.1f km", coaching.distanceKm)
            tvPhone.text = coaching.phone
            tvHours.text = coaching.hours

            btnNavigate.setOnClickListener { onNavigate(coaching) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachingVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCoachingBinding.inflate(inflater, parent, false)
        return CoachingVH(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CoachingVH, position: Int) =
        holder.bind(items[position])
}
