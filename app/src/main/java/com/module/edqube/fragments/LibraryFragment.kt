package com.module.edqube.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.module.edqube.R
import com.module.edqube.activities.LibraryProfileActivity
import com.module.edqube.adapters.LibraryAdapter
import com.module.edqube.databinding.FragmentLibraryBinding
import com.module.edqube.models.Library

class LibraryFragment : Fragment(R.layout.fragment_library) {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLibraryBinding.bind(view)

        // Sample demo data – replace with API or DB later
        val libraries = listOf(
            Library("Quantum Library", 3.2, "8353965811", "Open till 10:30 PM", R.drawable.libr),
            Library("City Central", 1.4, "9876543210", "Open till 09:00 PM", R.drawable.libr),
            Library("Readers' Hub", 4.8, "9123456780", "Open till 11:00 PM", R.drawable.libr),
            Library("Study Point", 2.0, "9865321470", "Open till 08:00 PM", R.drawable.libr),
            Library("Quantum Library", 3.2, "8353965811", "Open till 10:30 PM", R.drawable.libr),
            Library("City Central", 1.4, "9876543210", "Open till 09:00 PM", R.drawable.libr),
            Library("Readers' Hub", 4.8, "9123456780", "Open till 11:00 PM", R.drawable.libr),
            Library("Study Point", 2.0, "9865321470", "Open till 08:00 PM", R.drawable.libr)
        )



        binding.rvLibraries.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)   // 2 in a row
            adapter = LibraryAdapter(libraries) { lib ->
                val intent = Intent(requireContext(), LibraryProfileActivity::class.java)
                intent.putExtra("libraryName", lib.name)
                intent.putExtra("libraryDistance", lib.distanceKm)
                intent.putExtra("libraryPhone", lib.phone)
                intent.putExtra("libraryHours", lib.hours)
                intent.putExtra("libraryImage", lib.imageRes)
                startActivity(intent)
            }

            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
