package com.example.todoapplication.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.TaskRepository
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.databinding.ActivityAddNewTasksBinding
import com.example.todoapplication.domain.DeleteTaskUseCases
import com.example.todoapplication.domain.GetUseCase
import com.example.todoapplication.domain.InsertTaskUseCases
import com.example.todoapplication.domain.UpdateTaskUseCases
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
        binding.addBtn.setOnClickListener {
            val taskTitle = binding.addTitleEdTv.text.toString()
            val taskDes = binding.addSubTitleEdTv.text.toString()
            validateTitle()

            if (action == Action.ADD.name && taskTitle.isNotEmpty() && taskDes.isNotEmpty()) {
                taskViewModel.insertTask(taskId, taskTitle, taskDes)
                finish()
            } else if (action == Action.EDIT.name) {
                taskViewModel.updateTask(Tasks(taskId, taskTitle, taskDes))
                finish()
            }

        }
    }
    private fun setUpArguments() {
        taskId = intent.getIntExtra("taskId", 0)

        action = intent.getStringExtra("Action") ?: Action.ADD.name


        if (action == Action.EDIT.name) {
            taskViewModel.getTaskById(taskId)
        }
    }

    private fun setUpObserver() {
        taskViewModel.getSingleTask.observe(this) {
            binding.addTitleEdTv.setText(it.title)
            binding.addSubTitleEdTv.setText(it.description)
        }
    }

    fun validateTitle(){
        val taskTitle = binding.addTitleEdTv.text.toString()
        if(taskTitle.isEmpty()){
            binding.addTitleEdTv.error = "Title cannot between empty"
            return
        }
        if(taskTitle.length > 30){
            binding.addTitleEdTv.error = "The length should be less than 20 characters"
            return 
        }
        Toast.makeText(this, "Title is valid proceed", Toast.LENGTH_SHORT).show()
    }


}
