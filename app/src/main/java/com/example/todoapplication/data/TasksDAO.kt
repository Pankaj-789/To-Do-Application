package com.example.todoapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TasksDAO {

    @Insert
    suspend fun insertTask(tasks: Tasks)

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Query("Select * from Tasks")
    fun getTask() : LiveData<List<Tasks>>

//    @Query("Delete from Tasks where id = :id")
//    fun delete(id:Long)
}