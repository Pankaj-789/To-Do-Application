package com.example.todoapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.domain.usecases.InsertTaskUseCases
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.domain.usecases.DeleteTaskUseCases
import com.example.todoapplication.domain.usecases.GetUseCase
import com.example.todoapplication.domain.usecases.UpdateTaskUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(
    private val insertTaskUseCases: InsertTaskUseCases,
    private val deleteTaskUseCases: DeleteTaskUseCases,
    private val updateTaskUseCases: UpdateTaskUseCases,
    private val getUseCases: GetUseCase
) : ViewModel() {

    private val _getSingleTask: MutableLiveData<Tasks> = MutableLiveData<Tasks>()
    val getSingleTask: LiveData<Tasks> = _getSingleTask

    val allTasks: LiveData<List<Tasks>> = getUseCases.getAlltasks()

    fun insertTask(id: Int, title: String, description: String) {
        val task = Tasks(id, title = title, description = description)
        viewModelScope.launch(Dispatchers.IO) {
            insertTaskUseCases.insert(task)
        }
    }

    fun deleteTask(tasks: Tasks) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteTaskUseCases.delete(tasks)
        }
    }

    fun updateTask(tasks: Tasks) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCases.update(tasks)
        }
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _getSingleTask.postValue(getUseCases.getTaskById(id))
        }

    }
}
