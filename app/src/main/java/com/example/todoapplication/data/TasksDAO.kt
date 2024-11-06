package com.example.todoapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface TasksDAO {

    @Upsert
    suspend fun insertTask(tasks: Tasks)

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Update
    suspend fun updateTask(tasks : Tasks)

    @Query("Select * from Tasks")
    fun getTask() : LiveData<List<Tasks>>

    @Query("Select * from Tasks where id= :id")
    fun getSingleTask(id : Int) : Tasks

//    @Query("SELECT * FROM tasks ORDER BY isPinned DESC, id ASC")
//    fun getAllTasks(): LiveData<List<Tasks>>

}