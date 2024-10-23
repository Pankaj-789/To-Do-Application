package com.example.todoapplication.presentation.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.TaskRepository
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.databinding.ActivityAddNewTasksBinding
import com.example.todoapplication.domain.DeleteTaskUseCases
import com.example.todoapplication.domain.GetUseCase
import com.example.todoapplication.domain.InsertTaskUseCases
import com.example.todoapplication.domain.UpdateTaskUseCases
import com.example.todoapplication.presentation.viewmodel.Action
import com.example.todoapplication.presentation.viewmodel.TaskViewModelFactory
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewTasksActivity : AppCompatActivity() {

    private lateinit var database: TaskDatabase
    private lateinit var taskViewModel: TasksViewModel
    private lateinit var binding: ActivityAddNewTasksBinding
    private var taskId : Int = 0
    private var action : String = Action.ADD.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
        setUpArguments()
        setUpClickListener()
        setUpObserver()
    }
    fun initialize() {
        database =
            Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()
        val repository = TaskRepository(database.tasksDao())
        val factory = TaskViewModelFactory(
            InsertTaskUseCases(repository), DeleteTaskUseCases(repository),
            UpdateTaskUseCases(repository), GetUseCase(repository)
        )
        taskViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]
    }
    fun setUpClickListener() {
        binding.doneBtn.setOnClickListener {
            val taskTitle = binding.addTitleEdTv.text.toString()
            val taskDes = binding.addSubTitleEdTv.text.toString()

            if (action==Action.ADD.name && taskTitle.isNotEmpty() && taskDes.isNotEmpty()) {
//                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
                taskViewModel.insertTask(taskId,taskTitle, taskDes)
                finish()
            }else if(action==Action.EDIT.name){
                taskViewModel.updateTask(Tasks(taskId,taskTitle,taskDes))
                finish()
            }
        }
    }
    fun setUpArguments() {
        taskId = intent.getIntExtra("taskId", 0)

        action = intent.getStringExtra("Action") ?: Action.ADD.name


        if (action == Action.EDIT.name) {
            taskViewModel.getTaskById(taskId)
        }


    }
    fun setUpObserver(){
        taskViewModel.getSingleTask.observe(this){
            binding.addTitleEdTv.setText(it.title)
            binding.addSubTitleEdTv.setText(it.description)
        }
    }
}
