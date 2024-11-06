package com.example.todoapplication.domain.usecases

import androidx.lifecycle.LiveData
import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.data.Tasks

class GetUseCase(private val taskRepository: TaskRepository) {
    fun getAlltasks(): LiveData<List<Tasks>> {
        return taskRepository.allTasks

    }

    suspend fun getTaskById(id: Int): Tasks {
        return taskRepository.getTaskById(id)
    }
}