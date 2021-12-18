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
import com.capstone.urskripsi.ui.content.home.task.add.AddTaskActivity
import com.capstone.urskripsi.ui.content.home.task.list.TaskAdapter
import com.capstone.urskripsi.ui.content.home.task.list.TaskViewModel
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
        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        viewModel.task.observe(viewLifecycleOwner, Observer(this::showRecyclerView))

        binding?.apply {
            layoutEmpty.btnTambahBaru.setOnClickListener(this@HomeFragment)
            layoutListTask.tvAddTask.setOnClickListener(this@HomeFragment)
        }

        retrieveData()
    }


    private fun showRecyclerView(task: PagedList<Task>) {
        binding?.layoutListTask?.apply {
            rvTask.setHasFixedSize(true)
            rvTask.layoutManager = LinearLayoutManager(context)

            val taskAdapter = TaskAdapter { it, isCompleted ->
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
        initialState()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child(FIREBASE_NAME).value
                    binding?.apply {
                        layoutHomeHeader.tvGreeting.text =
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
            layoutHomeHeader.root.hide()
        }
    }

    private fun finalState() {
        viewModel.task.observe(this, {
            binding?.apply {
                if (it.size == 0) {
                    progressBar.root.hide()
                    layoutEmpty.root.show()
                    layoutListTask.root.hide()
                    layoutHomeHeader.root.show()
                    layoutHomeHeader.layoutTaskProgress.root.hide()
                    layoutHomeHeader.tvDesc.text = getString(R.string.tambahkan_tugas)
                } else {
                    progressBar.root.hide()
                    layoutEmpty.root.hide()
                    layoutListTask.root.show()
                    layoutHomeHeader.root.show()
                    layoutHomeHeader.layoutTaskProgress.root.show()
                    layoutHomeHeader.tvDesc.text =
                        getString(R.string.yuk_lanjutkan_progress_skripsinya)
                }
            }
        })
    }

    override fun onClick(view: View?) {
        if (view != null) {
            binding?.apply {
                when (view.id) {
                    layoutEmpty.btnTambahBaru.id -> {
                        val intent = Intent(requireContext(), AddTaskActivity::class.java)
                        startActivity(intent)
                    }
                    layoutListTask.tvAddTask.id -> {
                        val intent = Intent(requireContext(), AddTaskActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}