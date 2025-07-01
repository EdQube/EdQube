package com.module.edqube.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.edqube.R
import com.module.edqube.adapters.RecentSearchAdapter
import com.module.edqube.adapters.RecentlyViewedAdapter
import com.module.edqube.databinding.FragmentSearchBinding
import com.module.edqube.models.RecentSearch
import com.module.edqube.models.RecentlyViewed

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val recentAdapter by lazy {
        RecentSearchAdapter(
            listOf(
                RecentSearch("Quantum Library", "Shyam Nagar, Kanpur"),
                RecentSearch("Quantum Library", "Shyam Nagar, Kanpur"),
                RecentSearch("Quantum Library", "Shyam Nagar, Kanpur")
            )
        ) { clicked -> /* handle click */ }
    }

    private val viewedAdapter by lazy {
        RecentlyViewedAdapter(
            listOf(
                RecentlyViewed(
                    R.drawable.libr, "Quantum Library",
                    "Lal Bangla, Lorem ipsum…", "₹660 /month"
                ),
                RecentlyViewed(
                    R.drawable.coaching, "Quantum Library",
                    "Lal Bangla, Lorem ipsum…", "₹660 /month"
                ),
                RecentlyViewed(
                    R.drawable.libr, "Quantum Library",
                    "Lal Bangla, Lorem ipsum…", "₹660 /month"
                )
            )
        ) { clicked -> /* handle click */ }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        // Recent Searches list
        rvRecentSearches.apply {
            adapter = recentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Recently Viewed list
        rvRecentlyViewed.apply {
            adapter = viewedAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // “Clear All” button
        tvClearAll.setOnClickListener {
            // e.g. clear recent searches, notify adapter
        }

        // “See All” button
        tvSeeAll.setOnClickListener {
            // Navigate to full list if you have one
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
