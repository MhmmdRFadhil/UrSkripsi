package com.capstone.urskripsi.ui.content.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.urskripsi.R
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.databinding.FragmentHomeBinding
import com.capstone.urskripsi.ui.ViewModelFactory
import com.capstone.urskripsi.ui.content.task.AddTaskActivity
import com.capstone.urskripsi.ui.content.task.list.TaskAdapter
import com.capstone.urskripsi.ui.content.task.list.TaskViewModel
import com.capstone.urskripsi.utils.Constant.Companion.FIREBASE_NAME
import com.capstone.urskripsi.utils.Utility.hide
import com.capstone.urskripsi.utils.Utility.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentHomeBinding? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var viewModel: TaskViewModel

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
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        viewModel.task.observe(viewLifecycleOwner, Observer(this::showRecyclerView))


        binding?.layoutListTask?.tvAddTask?.setOnClickListener(this@HomeFragment)

        retrieveData()
    }


    private fun showRecyclerView(task: PagedList<Task>) {
        binding?.layoutListTask?.apply {
            rvTask.setHasFixedSize(true)
            rvTask.layoutManager = LinearLayoutManager(context)

            val taskAdapter = TaskAdapter {it, isCompleted ->
                viewModel.completeTask(it, isCompleted)
            }
            taskAdapter.submitList(task)
            rvTask.adapter = taskAdapter
        }
    }

    private fun retrieveData() {
        val emailUser = mAuth.currentUser?.email
        val setEmail = emailUser?.replace('.', ',')
        databaseReference = FirebaseDatabase.getInstance().getReference("User/$setEmail/Data")
        databaseReference.keepSynced(true)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child(FIREBASE_NAME).value
                    binding?.apply {
                        layoutTaskProgress.tvGreeting.text =
                            getString(R.string.hello, name.toString())
                    }
                }
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
                binding?.layoutListTask?.tvAddTask?.id -> {
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