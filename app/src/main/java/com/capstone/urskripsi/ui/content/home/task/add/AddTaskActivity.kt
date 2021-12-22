package com.capstone.urskripsi.ui.content.home.task.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.capstone.urskripsi.R
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.databinding.ActivityAddTaskBinding
import com.capstone.urskripsi.ui.ViewModelFactory
import com.capstone.urskripsi.utils.Calculation
import com.capstone.urskripsi.utils.Utility.simpleToolbar


class AddTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        simpleToolbar(getString(R.string.buat_task_baru), binding.toolbar.root, true)

        binding.btnTambah.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnTambah.id -> {
                val factory = ViewModelFactory.getInstance(this)
                val viewModel = ViewModelProvider(this, factory)[AddTaskViewModel::class.java]

                val newTask = Task(
                    0,
                    binding.edtTitle.text.toString().trim(),
                    binding.edtDescription.text.toString().trim(),
                    false
                )
                Calculation.countAllData += 1

                viewModel.addTask(newTask)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}