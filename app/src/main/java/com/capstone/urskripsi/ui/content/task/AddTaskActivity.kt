package com.capstone.urskripsi.ui.content.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.urskripsi.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}