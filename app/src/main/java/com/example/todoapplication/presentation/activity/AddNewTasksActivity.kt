package com.example.todoapplication.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.databinding.ActivityAddNewTasksBinding
import com.example.todoapplication.domain.usecases.DeleteTaskUseCases
import com.example.todoapplication.domain.usecases.GetUseCase
import com.example.todoapplication.domain.usecases.InsertTaskUseCases
import com.example.todoapplication.domain.usecases.UpdateTaskUseCases
import com.example.todoapplication.presentation.util.Action
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import com.example.todoapplication.presentation.viewmodel.factory.TaskViewModelFactory

class AddNewTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTasksBinding
    private lateinit var database: TaskDatabase
    private lateinit var taskViewModel: TasksViewModel
    private var taskId: Int = 0
    private var action: String = Action.ADD.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setUpArguments()
        setUpClickListener()
        setUpObserver()
    }

    private fun initialize() {
        database =
            Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()
        val repository = TaskRepository(database.tasksDao())
        val factory = TaskViewModelFactory(
            InsertTaskUseCases(repository), DeleteTaskUseCases(repository),
            UpdateTaskUseCases(repository), GetUseCase(repository)
        )
        taskViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]
    }

    private fun setUpClickListener() {
        binding.btnAdd.setOnClickListener {
            val taskTitle = binding.etvAddTitle.text.toString().trim()
            val taskDes = binding.etvAddDescription.text.toString().trim()

            taskViewModel.titleError.observe(this) { error ->
                binding.etvAddTitle.error = error
            }
            taskViewModel.descriptionError.observe(this) { error ->
                binding.etvAddDescription.error = error
            }

            if (action == Action.ADD.name) {
                taskViewModel.validateAndInsertTask(taskTitle, taskDes, taskId)
                taskViewModel.titleError.observe(this) { error ->
                    if (error == null && taskDes.isNotEmpty()) {
                        Toast.makeText(this, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else if (action == Action.EDIT.name) {
                if (taskViewModel.titleError.value == null && taskViewModel.descriptionError.value == null) {
                    taskViewModel.updateTask(Tasks(taskId, taskTitle, taskDes))
                    Toast.makeText(this, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }


    private fun setUpArguments() {
        taskId = intent.getIntExtra("taskId", 0)
        action = intent.getStringExtra("Action") ?: Action.ADD.name

        if (action == Action.EDIT.name) {
            supportActionBar?.title = "Update Task"
            taskViewModel.getTaskById(taskId)
        } else {
            supportActionBar?.title = "Add New Task"
        }
    }

    private fun setUpObserver() {
        taskViewModel.getSingleTask.observe(this) {
            binding.etvAddTitle.setText(it.title)
            binding.etvAddDescription.setText(it.description)
        }

        taskViewModel.titleError.observe(this) { error ->
            binding.etvAddTitle.error = error

        }
        taskViewModel.descriptionError.observe(this) { error ->
            binding.etvAddDescription.error = error
        }

    }
}
