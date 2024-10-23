package com.example.todoapplication.data

import androidx.lifecycle.LiveData

class TaskRepository(private val tasksDAO: TasksDAO )  {

    val allTasks : LiveData<List<Tasks>> = tasksDAO.getTask()

    suspend fun insertTask(tasks : Tasks) {
        tasksDAO.insertTask(tasks)
    }
    suspend fun deleteTask(tasks: Tasks){
        tasksDAO.deleteTask(tasks)
    }
    suspend fun updateTask(tasks: Tasks){
        tasksDAO.updateTask(tasks)
    }
    suspend fun getTaskById(id : Int) : Tasks{
        return tasksDAO.getSingleTask(id)

    }

}