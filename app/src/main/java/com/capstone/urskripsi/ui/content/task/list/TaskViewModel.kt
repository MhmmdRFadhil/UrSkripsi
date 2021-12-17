package com.capstone.urskripsi.ui.content.task.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.capstone.urskripsi.R
import com.capstone.urskripsi.data.Task
import com.capstone.urskripsi.data.TaskRepository
import com.capstone.urskripsi.utils.Event
import com.capstone.urskripsi.utils.TaskFilterType
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository): ViewModel() {
    private val _filter = MutableLiveData<TaskFilterType>()

    val task: LiveData<PagedList<Task>> = _filter.switchMap {
        taskRepository.getTasks(it)
    }

    private val _snackBarText = MutableLiveData<Event<Int>>()
    val snackBarText: LiveData<Event<Int>> = _snackBarText

    init {
        _filter.value = TaskFilterType.ALL_TASKS
    }

    fun filter(filterType: TaskFilterType) {
        _filter.value = filterType
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        taskRepository.completeTask(task, completed)
        if (completed) _snackBarText.value = Event(R.string.task_marked_complete)
        else _snackBarText.value = Event(R.string.task_marked_active)
    }
}