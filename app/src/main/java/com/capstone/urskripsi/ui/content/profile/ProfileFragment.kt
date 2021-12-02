package com.capstone.urskripsi.ui.content.profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentProfileBinding
import com.capstone.urskripsi.ui.content.profile.account.ChangePasswordActivity
import com.capstone.urskripsi.ui.content.profile.account.ChangeProfileActivity
import com.capstone.urskripsi.ui.login.LoginActivity
import com.capstone.urskripsi.utils.DividerItemDecorator
import com.capstone.urskripsi.utils.Utility.setTitleColor
import com.capstone.urskripsi.utils.Utility.showToast
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var accountAdapter: ProfileAdapter
    private lateinit var aboutAdapter: ProfileAdapter
    private lateinit var mAuth: FirebaseAuth

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
                setBackgroundDrawable(ColorDrawable(Color.parseColor(WHITE)))
            }
        }

        mAuth = FirebaseAuth.getInstance()

        signingOut()
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
            accountAdapter = ProfileAdapter(account)
            adapter = accountAdapter

            accountAdapter.setOnItemClickedCallback(object : OnItemClickedCallback {
                override fun onItemClick(position: Int) {
                    when (position) {
                        0 -> startActivity(Intent(context, ChangeProfileActivity::class.java))
                        1 -> startActivity(Intent(context, ChangePasswordActivity::class.java))
                    }
                }
            })
        }

        binding?.rvAbout?.apply {
            val about: ArrayList<Profile> = ArrayList()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            about.add(Profile(R.drawable.ic_about, getString(R.string.developer)))
            aboutAdapter = ProfileAdapter(about)
            adapter = aboutAdapter

            aboutAdapter.setOnItemClickedCallback(object : OnItemClickedCallback {
                override fun onItemClick(position: Int) {

                }
            })
        }
    }

    private fun signingOut() {
        binding?.viewLogout?.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            showToast(resources.getString(R.string.logout_auth), requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val WHITE = "#FFFFFF"
    }
}