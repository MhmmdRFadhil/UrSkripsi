package com.capstone.urskripsi.ui.content.home.task.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.databinding.ActivityAddTaskBinding
import com.capstone.urskripsi.ui.ViewModelFactory


class AddTaskActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                viewModel.addTask(newTask)
                finish()
            }
        }
    }
}