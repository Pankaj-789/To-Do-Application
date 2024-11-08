package com.example.todoapplication.presentation.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapplication.domain.usecases.DeleteTaskUseCases
import com.example.todoapplication.domain.usecases.GetUseCase
import com.example.todoapplication.domain.usecases.InsertTaskUseCases
import com.example.todoapplication.domain.usecases.UpdateTaskUseCases
import com.example.todoapplication.presentation.viewmodel.TasksViewModel

class TaskViewModelFactory(
    private val insertTaskUseCases: InsertTaskUseCases,
    private val deleteTaskUseCases: DeleteTaskUseCases,
    private val updateTaskUseCases: UpdateTaskUseCases,
    private val getUseCases: GetUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(
                insertTaskUseCases,
                deleteTaskUseCases,
                updateTaskUseCases,
                getUseCases
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}