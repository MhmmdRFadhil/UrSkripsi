package com.capstone.urskripsi.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @NonNull
    @ColumnInfo(name = "bab")
    val bab: String,

    @NonNull
    @ColumnInfo(name = "subBab")
    val subBab: String
)
