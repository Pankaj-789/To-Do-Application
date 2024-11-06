package com.example.todoapplication.domain.usecases

import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.data.Tasks

class UpdateTaskUseCases(private val taskRepository: TaskRepository) {
    suspend fun update(tasks: Tasks) {
        taskRepository.insertTask(tasks)
    }
}