package com.capstone.urskripsi.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.capstone.urskripsi.databinding.ActivityDetailTaskBinding
import com.capstone.urskripsi.ui.ViewModelFactory

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDetailContent()
    }

    private fun setDetailContent() {
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[DetailTaskViewModel::class.java]

        val extras = intent.extras

        if (extras != null) {
            viewModel.setTaskId(extras.getInt(TASK_ID, 0))
        }

        viewModel.task.observe(this, {
            if (it != null) {
                with(binding) {
                    edtTitle.setText(it.title)
                    edtDescription.setText(it.description)
                }
            }
        })

        binding.btnDelete.setOnClickListener {
            viewModel.deleteTask()
            finish()
        }
    }
}