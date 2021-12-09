package com.capstone.urskripsi.ui.content.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.MainActivity
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.FragmentLeaderBoardBinding
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PROGRESS
import com.capstone.urskripsi.utils.Utility.simpleToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LeaderBoardFragment : Fragment() {

    private var binding: FragmentLeaderBoardBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var leaderBoard: ArrayList<LeaderBoard>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as MainActivity).simpleToolbar(
                getString(R.string.leaderboard),
                binding?.toolbar?.root,
                false
            )
        }

        mAuth = FirebaseAuth.getInstance()
        leaderBoard = ArrayList()

        retrieveData()
    }

    private fun retrieveData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        databaseReference.keepSynced(true)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    leaderBoard.clear()
                    snapshot.children.forEach { email ->
                        email.children.forEach { data ->
                            val name = data.child(FIREBASE_NAME).value
                            val photo = data.child(FIREBASE_PHOTO).value
                            val progress = data.child(FIREBASE_PROGRESS).value
                            if (progress != null) {
                                leaderBoard.add(
                                    LeaderBoard(
                                        photo.toString(),
                                        name.toString(),
                                        progress.toString()
                                    )
                                )
                            }
                        }
                        leaderBoard.sortByDescending {
                            it.progressBar?.toInt()
                        }
                        showRecyclerView()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun showRecyclerView() {
        binding?.apply {
            rvLeaderboard.setHasFixedSize(true)
            rvLeaderboard.layoutManager = LinearLayoutManager(context)
            val leaderBoardAdapter = LeaderBoardAdapter(leaderBoard)
            rvLeaderboard.adapter = leaderBoardAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}