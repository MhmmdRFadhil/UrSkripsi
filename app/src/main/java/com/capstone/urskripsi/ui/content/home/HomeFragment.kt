package com.capstone.urskripsi.ui.content.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentHomeBinding
import com.capstone.urskripsi.utils.Utility.simpleToolbar

class HomeFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as MainActivity).simpleToolbar(
                getString(R.string.home),
                binding?.toolbar?.root,
                false
            )

            binding?.apply {
                btnAdd.setOnClickListener(this@HomeFragment)
            }
        }
    }


    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                binding?.btnAdd?.id -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}