package com.capstone.urskripsi.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {

    fun getFilteredQuery(filter: TaskFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM tasks ")
        when (filter) {
            TaskFilterType.COMPLETED_TASKS -> {
                simpleQuery.append("WHERE completed = 1")
            }
            TaskFilterType.ACTIVE_TASKS -> {
                simpleQuery.append("WHERE completed = 0")
            }
            else -> {
                // ALL_TASKS
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}