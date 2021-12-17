package com.capstone.urskripsi.ui.content.task.list

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.capstone.urskripsi.R
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.ui.content.task.list.CustomTaskTitleView.Companion.DONE
import com.capstone.urskripsi.ui.content.task.list.CustomTaskTitleView.Companion.NORMAL
import com.capstone.urskripsi.ui.detail.DetailTaskActivity
import com.capstone.urskripsi.utils.Constant.Companion.TASK_ID

class TaskAdapter(private val onCheckedChange: (Task, Boolean) -> Unit) :
    PagedListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val tvTitle: CustomTaskTitleView = itemView.findViewById(R.id.tv_title)
        val cbComplete: CheckBox = itemView.findViewById(R.id.cb_completed)
        lateinit var getTask: Task

        fun bind(task: Task) {
            getTask = task
            tvTitle.text = task.title
            Log.d("Title", task.title)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailTaskActivity::class.java)
                intent.putExtra(TASK_ID, task.id)
                itemView.context.startActivity(intent)
            }
            cbComplete.setOnClickListener {
                onCheckedChange(task, !task.isCompleted)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) as Task
        holder.bind(task)
        when {
            task.isCompleted -> {
                holder.cbComplete.isChecked = true
                holder.tvTitle.state = DONE
            }
            else -> {
                holder.cbComplete.isChecked = false
                holder.tvTitle.state = NORMAL
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


