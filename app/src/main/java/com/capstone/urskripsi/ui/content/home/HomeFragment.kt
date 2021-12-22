package com.capstone.urskripsi.ui.content.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.R
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.databinding.FragmentHomeBinding
import com.capstone.urskripsi.ui.ViewModelFactory
import com.capstone.urskripsi.ui.content.home.task.add.AddTaskActivity
import com.capstone.urskripsi.ui.content.home.task.list.TaskAdapter
import com.capstone.urskripsi.ui.content.home.task.list.TaskViewModel
import com.capstone.urskripsi.utils.Calculation
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
        calculationProgress()

        binding?.apply {
            layoutEmpty.btnTambahBaru.setOnClickListener(this@HomeFragment)
            layoutListTask.tvAddTask.setOnClickListener(this@HomeFragment)
            layoutHomeHeader.imgSort.setOnClickListener(this@HomeFragment)
        }

        retrieveData()
        initAction()
    }

    private fun showFilter() {
        val taskFilterBottomSheetFragment = TaskFilterBottomSheetFragment(viewModel)
        taskFilterBottomSheetFragment.show(parentFragmentManager, TAG)
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

        if (Calculation.countAllData != 0.0) {
            val count = ((Calculation.countAllDataIsCompleted / Calculation.countAllData) * 100).toInt()
            binding?.layoutHomeHeader?.layoutTaskProgress?.progressIndicatorBar?.progress =
                count
            binding?.layoutHomeHeader?.layoutTaskProgress?.tvNumberPercentage?.text =
                count.toString()

            Log.d(TAG, CALCULATION + "$count")
        }
    }

    private fun calculationProgress() {

        viewModel.getCountAllTasks().observe(viewLifecycleOwner, {
            Calculation.countAllData = it.toDouble()

            Log.d(TAG, TOTAL + "${Calculation.countAllData}")

        })

        viewModel.getCountIsCompleted().observe(viewLifecycleOwner, {
            Calculation.countAllDataIsCompleted = it.toDouble()
            Log.d(TAG, TOTAL_COMPLETE + "${Calculation.countAllDataIsCompleted}")

        })
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

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as TaskAdapter.TaskViewHolder).getTask
                viewModel.deleteTask(task)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding?.layoutListTask?.rvTask)
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
                    layoutHomeHeader.imgSort.id -> showFilter()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
        private const val CALCULATION = "Calculation : "
        private const val TOTAL = "Total : "
        private const val TOTAL_COMPLETE = "TotalComplete : "
    }
}