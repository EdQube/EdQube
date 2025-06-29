package com.module.edqube.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.module.edqube.R
import com.module.edqube.adapters.CoachingAdapter
import com.module.edqube.databinding.FragmentCoachingBinding
import com.module.edqube.models.Coaching

class CoachingFragment : Fragment(R.layout.fragment_coaching) {

    private var _binding: FragmentCoachingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCoachingBinding.bind(view)

        // Sample demo data – replace with API or DB later
        val coachings = listOf(
            Coaching("Apex Institute", 2.5, "9998887777", "Open till 07:00 PM", R.drawable.coaching),
            Coaching("Bright Academy", 1.1, "8887776666", "Open till 08:30 PM", R.drawable.coaching),
            Coaching("Pathshala Classes", 3.4, "7776665555", "Open till 09:00 PM", R.drawable.coaching),
            Coaching("EduWings", 4.0, "6665554444", "Open till 10:00 PM", R.drawable.coaching),
            Coaching("Apex Institute", 2.5, "9998887777", "Open till 07:00 PM", R.drawable.coaching),
            Coaching("Bright Academy", 1.1, "8887776666", "Open till 08:30 PM", R.drawable.coaching),
            Coaching("Pathshala Classes", 3.4, "7776665555", "Open till 09:00 PM", R.drawable.coaching),
            Coaching("EduWings", 4.0, "6665554444", "Open till 10:00 PM", R.drawable.coaching)
        )

        binding.rvCoachings.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = CoachingAdapter(coachings) { coaching ->
                // TODO: open maps or detail screen
            }
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
