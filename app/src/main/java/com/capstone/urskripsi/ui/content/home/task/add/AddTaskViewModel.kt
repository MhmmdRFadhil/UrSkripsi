package com.capstone.urskripsi.ui.content.home.task.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun addTask(newTask: Task) {
        viewModelScope.launch {
            repository.insertTask(newTask)
        }
    }
}