package com.example.todoapplication.domain

import androidx.lifecycle.LiveData
import com.example.todoapplication.data.TaskRepository
import com.example.todoapplication.data.Tasks

class InsertTaskUseCases(private val taskRepository: TaskRepository) {
    suspend fun insert(tasks: Tasks) {
        taskRepository.insertTask(tasks)
    }
}

class DeleteTaskUseCases(private val taskRepository: TaskRepository){
    suspend fun delete(tasks : Tasks){
        taskRepository.deleteTask(tasks)
    }
}
class UpdateTaskUseCases(private val taskRepository: TaskRepository){
    suspend fun update(tasks: Tasks){
        taskRepository.updateTask(tasks)
    }
}
class GetUseCase(private val taskRepository: TaskRepository){
    fun getAlltasks() : LiveData<List<Tasks>>{
        return taskRepository.allTasks

    }
    suspend fun getTaskById(id : Int): Tasks{
        return taskRepository.getTaskById(id)
    }
}