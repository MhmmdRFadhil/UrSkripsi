package com.capstone.urskripsi.ui.content.home.task.list

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.databinding.ItemRowListTaskBinding
import com.capstone.urskripsi.ui.content.home.task.list.CustomTaskTitleView.Companion.DONE
import com.capstone.urskripsi.ui.content.home.task.list.CustomTaskTitleView.Companion.NORMAL
import com.capstone.urskripsi.ui.content.home.task.detail.DetailTaskActivity
import com.capstone.urskripsi.utils.Constant.Companion.TASK_ID

class TaskAdapter(private val onCheckedChange: (Task, Boolean) -> Unit) :
    PagedListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = ItemRowListTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) as Task
        holder.bind(task)
        with(holder.binding) {
            when {
                task.isCompleted -> {
                    cbCompleted.isChecked = true
                    tvTitle.state = DONE
                }
                else -> {
                    cbCompleted.isChecked = false
                    tvTitle.state = NORMAL
                }
            }
        }
    }

    inner class TaskViewHolder(val binding: ItemRowListTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var getTask: Task

        fun bind(task: Task) {
            getTask = task
            with(binding) {
                tvTitle.text = task.title
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailTaskActivity::class.java)
                    intent.putExtra(TASK_ID, task.id)
                    itemView.context.startActivity(intent)
                }
                cbCompleted.setOnClickListener {
                    onCheckedChange(task, !task.isCompleted)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }

        }
    }
}


