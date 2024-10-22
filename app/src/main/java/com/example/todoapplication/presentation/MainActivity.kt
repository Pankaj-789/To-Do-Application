package com.example.todoapplication.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.todoapplication.databinding.ActivityMainBinding
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.presentation.adapter.TasksListAdapter
import com.example.todoapplication.presentation.viewmodel.TasksViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var database: TaskDatabase
    lateinit var binding : ActivityMainBinding
    lateinit var tasksListAdapter: TasksListAdapter
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext,TaskDatabase::class.java,"taskDB").build()

        tasksListAdapter = TasksListAdapter{ task->
            GlobalScope.launch {
                database.tasksDao().deleteTask(task)
            }
        }

        binding.taskRv.apply{
            adapter = tasksListAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    private fun getData() {
        database.tasksDao().getTask().observe(this) {tasks ->
            if(tasks != null){
                tasksListAdapter.submitList(tasks)
            }

        }
    }

    fun addNewTask(view: View) {
        val intent = Intent(this,AddNewTasks::class.java)
        startActivity(intent)

    }

    override fun onResume() {

        super.onResume()
        getData()

    }


}