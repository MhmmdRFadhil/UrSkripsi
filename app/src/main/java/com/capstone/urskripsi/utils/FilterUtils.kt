package com.capstone.urskripsi.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object FilterUtils {

    fun getFilteredQuery(filter: TaskFilterType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM tasks ")
        when (filter) {
            TaskFilterType.COMPLETED_TASKS -> {
                simpleQuery.append("WHERE is_completed = 1")
            }
            else -> {}
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}