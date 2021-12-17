package com.capstone.urskripsi.ui.detail

import androidx.lifecycle.*
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.data.TaskRepository
import kotlinx.coroutines.launch

class DetailTaskViewModel(private val repository: TaskRepository): ViewModel() {

    private val _taskId = MutableLiveData<Int>()

    private val _task = _taskId.switchMap { id ->
        repository.getTaskById(id)
    }
    val task: LiveData<Task> = _task

    fun setTaskId(taskId: Int) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId
    }

    fun deleteTask() {
        viewModelScope.launch {
            _task.value?.let { repository.deleteTask(it) }
        }
    }
}