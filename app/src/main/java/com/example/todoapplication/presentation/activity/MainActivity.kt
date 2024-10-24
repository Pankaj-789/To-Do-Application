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
import com.example.todoapplication.presentation.util.Action
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import com.example.todoapplication.presentation.viewmodel.factory.TaskViewModelFactory
import kotlinx.coroutines.launch

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
            }
        }
    }
    private fun addNewTask() {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        startActivity(intent)
    }

    private fun editTask(id : Int) {
        val intent = Intent(this, AddNewTasksActivity::class.java)
        intent.putExtra("taskId",id)
        intent.putExtra("Action",Action.EDIT.name)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
    private fun setUpInitialiser(){
        database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()
        val repository = TaskRepository(database.tasksDao())
        val factory = TaskViewModelFactory(InsertTaskUseCases(repository), DeleteTaskUseCases(repository),
            UpdateTaskUseCases(repository), GetUseCase(repository))

        tasksViewModel = ViewModelProvider(this, factory)[TasksViewModel::class.java]

        tasksListAdapter = TasksListAdapter({ task ->
            tasksViewModel.deleteTask(task)
        }, { task ->
            editTask(task.id)
        })
    }
    private fun setUpAdapter(){
        binding.taskRv.apply {
            adapter = tasksListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    private fun setUpObserver(){
        tasksViewModel.allTasks.observe(this) { tasks ->
            tasksListAdapter.submitList(tasks)
        }
    }
    private fun setUpListener(){
        binding.addTaskFAB.setOnClickListener{
            addNewTask()
        }
    }
}