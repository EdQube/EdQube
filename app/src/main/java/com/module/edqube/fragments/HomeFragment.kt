package com.module.edqube.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.module.edqube.R
import com.module.edqube.adapters.HomePagerAdapter
import com.module.edqube.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If you want this toolbar to act as the Activity’s action bar:
        //(requireActivity() as AppCompatActivity).setSupportActionBar(binding.topToolbar)

        binding.viewPager.adapter = HomePagerAdapter(this)   // see next section

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            val tabView = layoutInflater.inflate(R.layout.custom_tab, null) as TextView
            tabView.text = when (pos) {
                0 -> "Libraries"
                1 -> "Posts"
                else -> "Coachings"
            }
            tabView.typeface = resources.getFont(R.font.mregular)
            tab.customView = tabView
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
