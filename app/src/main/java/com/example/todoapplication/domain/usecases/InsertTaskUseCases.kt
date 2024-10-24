package com.example.todoapplication.domain.usecases

import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.data.Tasks

class InsertTaskUseCases(private val taskRepository: TaskRepository) {
    suspend fun insert(tasks: Tasks) {
        taskRepository.insertTask(tasks)
    }
}