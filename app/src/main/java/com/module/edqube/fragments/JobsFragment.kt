package com.module.edqube.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.edqube.R
import com.module.edqube.adapters.JobsAdapter
import com.module.edqube.databinding.FragmentJobsBinding
import com.module.edqube.models.JobUpdate      // ✅ add this import

class JobsFragment : Fragment(R.layout.fragment_jobs) {

    private val jobsAdapter = JobsAdapter()   // renamed for clarity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentJobsBinding.bind(view)

        // Back arrow
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView setup
        binding.recyclerJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobsAdapter
        }

        // Demo list
        jobsAdapter.submitList(
            listOf(
                JobUpdate(
                    R.drawable.libr,
                    "Bihar Police",
                    "Bihar Police CSBC Constable Exam City Details 2025 – Out",
                    "9.56 AM"
                ),
                JobUpdate(
                    R.drawable.coaching,
                    "Bihar Special School Teacher",
                    "Bihar BPSC Special School Teacher Online Form 2025 (7279 Post)",
                    "9.56 AM"
                ),
                JobUpdate(
                    R.drawable.libr,
                    "Indian Army B. Sc Nursing Course",
                    "Indian Army B.Sc Nursing Course Online Form 2025 – Start",
                    "9.56 AM"
                )
            )
        )
    }
}
