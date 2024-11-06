package com.example.todoapplication.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.repository.TaskRepository
import com.example.todoapplication.databinding.ActivityMainBinding
import com.example.todoapplication.domain.usecases.DeleteTaskUseCases
import com.example.todoapplication.domain.usecases.GetUseCase
import com.example.todoapplication.domain.usecases.InsertTaskUseCases
import com.example.todoapplication.domain.usecases.UpdateTaskUseCases
import com.example.todoapplication.presentation.adapter.TasksListAdapter
import com.example.todoapplication.presentation.util.Action
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import com.example.todoapplication.presentation.viewmodel.factory.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TaskDatabase
    private lateinit var tasksListAdapter: TasksListAdapter
    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpInitialiser()
        setUpAdapter()
        setUpObserver()
        setUpListener()
    }

    private fun getData() {
        database.tasksDao().getTask().observe(this) { tasks ->
            if (tasks != null) {
                tasksListAdapter.submitList(tasks)
                binding.tvNoRecords.isVisible = tasks.isEmpty()
            }
        }
    }

    private fun addNewTask() {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        startActivity(intent)
    }

    private fun editTask(id: Int) {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        intent.putExtra("taskId", id)
        intent.putExtra("Action", Action.EDIT.name)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun setUpInitialiser() {
        database =
            Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()
        val repository = TaskRepository(database.tasksDao())
        val factory = TaskViewModelFactory(
            InsertTaskUseCases(repository), DeleteTaskUseCases(repository),
            UpdateTaskUseCases(repository), GetUseCase(repository)
        )

        tasksViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]

        tasksListAdapter = TasksListAdapter({ task ->
            tasksViewModel.deleteTask(task)
        }, { task ->
            editTask(task.id)
        })
    }

    private fun setUpAdapter() {
        binding.rvTask.apply {
            adapter = tasksListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setUpObserver() {
        tasksViewModel.allTasks.observe(this) { tasks ->
            if (tasks == null) return@observe
            tasksListAdapter.submitList(tasks)
            binding.tvNoRecords.isVisible = tasks.isEmpty()
        }
    }

    private fun setUpListener() {
        binding.fabAddTask.setOnClickListener {
            addNewTask()
        }
    }

}