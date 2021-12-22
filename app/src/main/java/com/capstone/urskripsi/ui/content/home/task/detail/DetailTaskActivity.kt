package com.capstone.urskripsi.ui.content.home.task.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.capstone.urskripsi.R
import com.capstone.urskripsi.databinding.ActivityDetailTaskBinding
import com.capstone.urskripsi.ui.ViewModelFactory
import com.capstone.urskripsi.utils.Calculation
import com.capstone.urskripsi.utils.Constant.Companion.TASK_ID
import com.capstone.urskripsi.utils.Utility.simpleToolbar

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        simpleToolbar(getString(R.string.detail_task), binding.toolbar.root, true)

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
            Calculation.countAllData -= 1
            finish()
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