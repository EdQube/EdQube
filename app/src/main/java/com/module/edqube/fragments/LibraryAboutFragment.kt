package com.module.edqube.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.module.edqube.R
import com.module.edqube.databinding.FragmentLibraryAboutBinding
import com.module.edqube.databinding.AboutItemBinding

class LibraryAboutFragment : Fragment() {

    private var _binding: FragmentLibraryAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        tvDescription.text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."

        // Access bindings of included layouts
        bindRow(ownerItem, R.drawable.user, "Ankit Kumar")
        bindRow(villageItem, R.drawable.user, "Lal Bangala")
        bindRow(cityItem, R.drawable.user, "Unasi, Uttar Pradesh")
        bindRow(pincodeItem, R.drawable.user, "209252")
        bindRow(seatItem, R.drawable.user, "25")
    }

    private fun bindRow(itemBinding: AboutItemBinding, iconRes: Int, value: String) {
        itemBinding.ivAboutIcon.setImageResource(iconRes)
        itemBinding.tvAboutValue.text = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
