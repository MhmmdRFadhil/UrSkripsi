package com.capstone.urskripsi.ui.content.profile.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.BottomSheetDialogDeveloperBinding
import com.capstone.urskripsi.utils.Utility.loadImageUrl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeveloperBottomSheetFragment(private val developer: Developer) : BottomSheetDialogFragment() {

    private var binding: BottomSheetDialogDeveloperBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogDeveloperBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
    }

    private fun getData() {
        binding?.apply {
            developerImg.loadImageUrl(developer.image.toString())
            tvDeveloperName.text = developer.name.toString()
            tvUniversityNameDeveloper.text = developer.universityName.toString()
            imgGithub.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.github)))
            }

            imgLinkedIn.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.linkedIn)))
            }

            imgInstagram.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(developer.instagram)))
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog_Rounded
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}