package com.example.todoapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tasks")
data class Tasks(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val title : String,
    val description : String)
