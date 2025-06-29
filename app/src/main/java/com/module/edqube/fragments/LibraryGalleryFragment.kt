package com.module.edqube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.module.edqube.R

class LibraryGalleryFragment : Fragment() {

    private val imageList = listOf(
        R.drawable.libr, R.drawable.libr, R.drawable.libr,
        R.drawable.libr, R.drawable.libr, R.drawable.libr
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_library_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvGallery)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = GalleryAdapter(imageList)
    }

    class GalleryAdapter(private val items: List<Int>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_image, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.img.setImageResource(items[position])
        }

        override fun getItemCount() = items.size

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val img: ImageView = view.findViewById(R.id.ivGalleryImage)
        }
    }
}
