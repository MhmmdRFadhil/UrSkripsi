package com.capstone.urskripsi.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.capstone.urskripsi.utils.FilterUtils
import com.capstone.urskripsi.utils.TaskFilterType

class TaskRepository(private val taskDao: TaskDao) {

    fun getTasks(filter: TaskFilterType): LiveData<PagedList<Task>> {
        val filterQuery = FilterUtils.getFilteredQuery(filter)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(PLACEHOLDERS)
            .setInitialLoadSizeHint(PAGE_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()
        return LivePagedListBuilder(taskDao.getTasks(filterQuery), config).build()
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTask(newTask: Task): Long {
        return taskDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun completeTask(task: Task, isCompleted: Boolean) {
        taskDao.updateCompleted(task.id, isCompleted)
    }

    fun getCountCompleted(): LiveData<Int>  {
        return taskDao.getCount()
    }

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDatabase.getInstance(context)
                    instance = TaskRepository(database.taskDao())
                }
                return instance as TaskRepository
            }
        }
    }
}