package com.capstone.urskripsi.ui.content.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentProfileBinding
import com.capstone.urskripsi.utils.DividerItemDecorator
import com.capstone.urskripsi.utils.Utility.setTitleColor

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.apply {
                title = getString(R.string.profile)
                setTitleColor(Color.BLACK)
                setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            }
        }

        showRecyclerView()
    }

    private fun showRecyclerView() {
        binding?.rvAccount?.apply {
            val account: ArrayList<Profile> = ArrayList()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            account.apply {
                add(Profile(R.drawable.ic_profile, getString(R.string.change_profile)))
                add(Profile(R.drawable.ic_change_password, getString(R.string.change_password)))
            }
            addItemDecoration(DividerItemDecorator(context))
            profileAdapter = ProfileAdapter(account)
            adapter = profileAdapter
        }

        binding?.rvAbout?.apply {
            val about: ArrayList<Profile> = ArrayList()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            about.add(Profile(R.drawable.ic_about, getString(R.string.developer)))
            profileAdapter = ProfileAdapter(about)
            adapter = profileAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}