package com.module.edqube.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.module.edqube.R
import com.module.edqube.adapters.ProfileAdapter
import com.module.edqube.databinding.DialogProfileMenuBinding
import com.module.edqube.databinding.FragmentProfileBinding
import com.module.edqube.initialScreens.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = listOf("Posts", "Likes", "Media")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingProfile.setOnClickListener {
            showProfileMenu()
        }


        binding.profileviewPager.adapter = ProfileAdapter(this)

        TabLayoutMediator(binding.profiletabLayout, binding.profileviewPager) { tab, position ->
            val tabView = layoutInflater.inflate(R.layout.custom_tab, null) as TextView
            tabView.text = tabTitles[position]
            tabView.typeface = resources.getFont(R.font.mregular)
            tab.customView = tabView
        }.attach()
    }

    private fun showProfileMenu() {
        val container = binding.sideMenuContainer

        container.removeAllViews() // clear previous
        val sideMenuBinding = DialogProfileMenuBinding.inflate(layoutInflater)

        container.addView(sideMenuBinding.root)
        container.visibility = View.VISIBLE

        // animate in
        container.translationX = container.width.toFloat()
        container.animate().translationX(0f).setDuration(300).start()

        // back button dismiss
        sideMenuBinding.btnClose.setOnClickListener {
            hideProfileMenu()
        }

        // logout click
        sideMenuBinding.logout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    hideProfileMenu()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireActivity(), LoginActivity::class.java) 
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // stop clicks from propagating
        sideMenuBinding.root.setOnClickListener {
            // do nothing, eat click
        }
    }

    private fun hideProfileMenu() {
        val container = binding.sideMenuContainer
        container.animate().translationX(container.width.toFloat()).withEndAction {
            container.visibility = View.GONE
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


