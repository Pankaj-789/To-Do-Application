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

    private val _titleError = MutableLiveData<String?>()
    val titleError: LiveData<String?> get() = _titleError

    private val _descriptionError = MutableLiveData<String?>()
    val descriptionError: LiveData<String?> get() = _descriptionError

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

    fun validateAndInsertTask(title: String, description: String, id: Int = 0) {
        var isValid = true
        if (title.isEmpty()) {
            _titleError.value = "Title cannot be empty"
            isValid = false
        } else {
            _titleError.value = null
        }
        if (description.isEmpty()) {
            _descriptionError.value = "Description cannot be empty"
            isValid = false
        } else {
            _descriptionError.value = null
        }
        if (isValid) {
            insertTask(id, title, description)
        }
    }
}
