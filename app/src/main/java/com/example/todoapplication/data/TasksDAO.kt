package com.example.todoapplication.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface TasksDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(tasks: Tasks)

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Update
    suspend fun updateTask(tasks : Tasks)

    @Query("Select * from Tasks")
    fun getTask() : LiveData<List<Tasks>>

    @Query("Select * from Tasks where id= :id")
    fun getSingleTask(id : Int) : Tasks

}