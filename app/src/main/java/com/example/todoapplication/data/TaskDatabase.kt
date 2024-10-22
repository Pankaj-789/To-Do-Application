package com.example.todoapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Tasks::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDAO
}