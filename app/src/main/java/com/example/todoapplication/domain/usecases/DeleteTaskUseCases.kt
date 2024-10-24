package com.example.todoapplication.domain.usecases

import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.data.Tasks

class DeleteTaskUseCases(private val taskRepository: TaskRepository){
    suspend fun delete(tasks : Tasks){
        taskRepository.deleteTask(tasks)

    }
}