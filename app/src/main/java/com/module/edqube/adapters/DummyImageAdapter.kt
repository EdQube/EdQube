package com.module.edqube.adapters

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R

class DummyImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<DummyImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val tv = TextView(parent.context).apply {
            setPadding(24, 24, 24, 24)
            background = parent.context.getDrawable(R.drawable.edittext_bg)
        }
        return ImageViewHolder(tv)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.textView.text = images[position]
    }

    override fun getItemCount(): Int = images.size
}
