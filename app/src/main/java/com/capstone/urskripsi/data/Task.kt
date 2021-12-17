package com.capstone.urskripsi.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.capstone.urskripsi.data.DataTask.DESCRIPTION
import com.capstone.urskripsi.data.DataTask.ID
import com.capstone.urskripsi.data.DataTask.TABLE_NAME
import com.capstone.urskripsi.data.DataTask.TITLE

@Entity(tableName = TABLE_NAME)
data class Task(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = ID)
    val id: Int = 0,

    @NonNull
    @ColumnInfo(name = TITLE)
    val title: String,

    @NonNull
    @ColumnInfo(name = DESCRIPTION)
    val description: String
)
