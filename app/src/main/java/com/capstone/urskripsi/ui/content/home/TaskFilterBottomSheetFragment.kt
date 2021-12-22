package com.capstone.urskripsi.ui.content.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.BottomSheetDialogSortTaskBinding
import com.capstone.urskripsi.ui.content.home.task.list.TaskViewModel
import com.capstone.urskripsi.utils.TaskFilterType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TaskFilterBottomSheetFragment(private val viewModel: TaskViewModel) :
    BottomSheetDialogFragment() {
    private var binding: BottomSheetDialogSortTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogSortTaskBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }


    private fun getData() {
        binding?.apply {
            rgSort.setOnCheckedChangeListener { _, p1 ->
                when (p1) {
                    R.id.rb_completed -> {
                        viewModel.filter(TaskFilterType.COMPLETED_TASKS)
                    }
                    else -> {
                        viewModel.filter(TaskFilterType.ALL_TASKS)
                    }
                }
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog_Rounded
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}