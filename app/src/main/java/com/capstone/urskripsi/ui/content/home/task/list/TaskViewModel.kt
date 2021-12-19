package com.capstone.urskripsi.ui.content.home.task.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.data.TaskRepository
import com.capstone.urskripsi.utils.TaskFilterType
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository): ViewModel() {
    private val _filter = MutableLiveData<TaskFilterType>()

    val task: LiveData<PagedList<Task>> = _filter.switchMap {
        taskRepository.getTasks(it)
    }

    init {
        _filter.value = TaskFilterType.ALL_TASKS
    }

    fun filter(filterType: TaskFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}