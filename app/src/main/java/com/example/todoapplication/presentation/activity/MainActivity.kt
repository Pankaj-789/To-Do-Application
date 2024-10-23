package com.example.todoapplication.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todoapplication.databinding.ActivityMainBinding
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.TaskRepository
import com.example.todoapplication.domain.DeleteTaskUseCases
import com.example.todoapplication.domain.GetUseCase
import com.example.todoapplication.domain.InsertTaskUseCases
import com.example.todoapplication.domain.UpdateTaskUseCases
import com.example.todoapplication.presentation.adapter.TasksListAdapter
import com.example.todoapplication.presentation.viewmodel.Action
import com.example.todoapplication.presentation.viewmodel.TaskViewModelFactory
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var database: TaskDatabase
    lateinit var binding: ActivityMainBinding
    lateinit var tasksListAdapter: TasksListAdapter
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()
        val repository = TaskRepository(database.tasksDao())
        val factory = TaskViewModelFactory(InsertTaskUseCases(repository), DeleteTaskUseCases(repository),
            UpdateTaskUseCases(repository), GetUseCase(repository)
        )
        tasksViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]



        tasksListAdapter = TasksListAdapter({ task ->
            tasksViewModel.deleteTask(task)

        }, { task ->
            editTask(task.id)

        })

        binding.taskRv.apply {
            adapter = tasksListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        tasksViewModel.allTasks.observe(this) { tasks ->
            tasksListAdapter.submitList(tasks)

        }
        binding.addTaskFab.setOnClickListener{
            addNewTask()
        }
    }


    private fun getData() {
        database.tasksDao().getTask().observe(this) { tasks ->
            if (tasks != null) {
                tasksListAdapter.submitList(tasks)
            }
        }
    }

    fun addNewTask() {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        startActivity(intent)

    }

    fun editTask(id : Int) {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        intent.putExtra("taskId",id)
        intent.putExtra("Action",Action.EDIT.name)
        startActivity(intent)
    }

    override fun onResume() {

        super.onResume()
        getData()

    }


}