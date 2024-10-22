package com.example.todoapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapplication.domain.DeleteTaskUseCases
import com.example.todoapplication.domain.GetUseCase
import com.example.todoapplication.domain.InsertTaskUseCases
import com.example.todoapplication.data.Tasks

class TasksViewModel(
    private val insertTaskUseCases: InsertTaskUseCases,
    private val getUseCases: GetUseCase,
    private val deleteTaskUseCases: DeleteTaskUseCases
)
    : ViewModel() {

    val allTasks : LiveData<List<Tasks>> = getUseCases.getRepositoryData()

    suspend fun insertTask(title : String, description : String) {
        val task = Tasks(0L,title = title,description = description)
        insertTaskUseCases.insert(task)
    }

    suspend fun deleteTask(tasks: Tasks){
        deleteTaskUseCases.delete(tasks)
    }




}
class TaskViewModelFactory(
    private val insertTaskUseCases: InsertTaskUseCases,
    private val getUseCases: GetUseCase,
    private val deleteTaskUseCases: DeleteTaskUseCases
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
        return TasksViewModel(insertTaskUseCases, getUseCases, deleteTaskUseCases) as T
    }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}