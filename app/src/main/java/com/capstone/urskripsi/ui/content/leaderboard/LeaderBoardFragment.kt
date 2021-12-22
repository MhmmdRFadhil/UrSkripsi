package com.capstone.urskripsi.ui.content.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.databinding.FragmentLeaderBoardBinding
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PHOTO
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_PROGRESS
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
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

        mAuth = FirebaseAuth.getInstance()
        leaderBoard = ArrayList()

        retrieveData()
    }

    private fun retrieveData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        databaseReference.keepSynced(true)
        initialState()
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
                                if (photo != null) {
                                    leaderBoard.add(
                                        LeaderBoard(
                                            photo.toString(),
                                            name.toString(),
                                            progress.toString().toInt()
                                        )
                                    )
                                } else {
                                    leaderBoard.add(
                                        LeaderBoard(
                                            null,
                                            name.toString(),
                                            progress.toString().toInt()
                                        )
                                    )
                                }
                            }
                        }
                        leaderBoard.sortByDescending {
                            it.progressBar
                        }
                        showRecyclerView()
                    }
                }
                finalState()
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

    private fun initialState() {
        binding?.apply {
            progressBar.root.show()
            rvLeaderboard.hide()
        }
    }

    private fun finalState() {
        binding?.apply {
            progressBar.root.hide()
            rvLeaderboard.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}