package com.capstone.urskripsi.ui.content.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentProfileBinding
import com.capstone.urskripsi.ui.content.profile.about.DeveloperActivity
import com.capstone.urskripsi.ui.content.profile.account.ChangePasswordActivity
import com.capstone.urskripsi.ui.content.profile.account.ChangeProfileActivity
import com.capstone.urskripsi.ui.login.LoginActivity
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.DividerItemDecorator
import com.capstone.urskripsi.utils.Utility.loadImageUrl
import com.capstone.urskripsi.utils.Utility.showToast
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null
    private lateinit var accountAdapter: ProfileAdapter
    private lateinit var aboutAdapter: ProfileAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

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
            (activity as MainActivity).simpleToolbar(
                getString(R.string.profile),
                binding?.toolbar?.root,
                false
            )
        }

        mAuth = FirebaseAuth.getInstance()

        signingOut()
        showRecyclerView()
        retrieveData()
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
                    if (position == 0) {
                        startActivity(Intent(context, DeveloperActivity::class.java))
                    }
                }
            })
        }
    }

    private fun retrieveData() {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference =
            FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.keepSynced(true)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child(FIREBASE_NAME).value
                    val photo = snapshot.child(FIREBASE_PHOTO).value
                    binding?.apply {

                        if (photo != null) {
                            imgProfile.loadImageUrl(photo.toString())
                        } else {
                            imgProfile.setImageResource(R.drawable.avatar)
                        }

                        tvProfileName.text = name.toString()
                        tvEmail.text = emailUser
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun signingOut() {

        binding?.viewLogout?.setOnClickListener {
            mAuth.currentUser?.getIdToken(false)?.addOnSuccessListener { result ->
                when (result.signInProvider) {
                    GOOGLE -> signOutGoogle()
                    PASSWORD -> signOutEmail()
                }
            }

            showToast(resources.getString(R.string.logout_auth), requireContext())
        }
    }

    private fun signOutGoogle() {
        GoogleSignIn.getClient(
            requireContext(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()
        mAuth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    private fun signOutEmail() {
        mAuth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val GOOGLE = "google.com"
        const val PASSWORD = "password"
    }
}