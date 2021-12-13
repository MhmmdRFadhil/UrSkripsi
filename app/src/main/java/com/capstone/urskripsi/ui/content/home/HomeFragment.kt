package com.capstone.urskripsi.ui.content.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentHomeBinding
import com.capstone.urskripsi.ui.content.task.AddTaskActivity
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentHomeBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        binding?.layoutEmpty?.btnTambahBaru?.setOnClickListener(this@HomeFragment)

        retrieveData()
    }

    private fun retrieveData() {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.keepSynced(true)
        initialState()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child(FIREBASE_NAME).value
                    binding?.apply {
                        layoutTaskProgress.tvGreeting.text =
                            getString(R.string.hello, name.toString())
                    }
                }
                finalState()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initialState() {
        binding?.apply {
            progressBar.root.show()
            layoutEmpty.root.hide()
            layoutTaskProgress.root.hide()
        }
    }

    private fun finalState() {
        binding?.apply {
            progressBar.root.hide()
            layoutEmpty.root.show()
            layoutTaskProgress.root.show()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                binding?.layoutEmpty?.btnTambahBaru?.id -> {
                    val intent = Intent(requireContext(), AddTaskActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}