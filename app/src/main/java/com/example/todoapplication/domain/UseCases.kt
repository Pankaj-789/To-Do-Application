package com.example.todoapplication.domain

import androidx.lifecycle.LiveData
import com.example.todoapplication.repository.TaskRepository
import com.example.todoapplication.data.Tasks

class InsertTaskUseCases(private val repository: TaskRepository) {
    suspend fun insert(tasks: Tasks) {
        repository.insertTask(tasks)
    }
}

class DeleteTaskUseCases(private val repository: TaskRepository){
    suspend fun delete(tasks : Tasks){
        repository.deleteTask(tasks)
    }
}

class GetUseCase(private val repository: TaskRepository){
    fun getRepositoryData() : LiveData<List<Tasks>>{
        return repository.allTasks

    }
}