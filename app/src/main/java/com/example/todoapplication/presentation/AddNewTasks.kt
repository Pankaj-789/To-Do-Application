package com.example.todoapplication.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.todoapplication.R
import com.example.todoapplication.data.TaskDatabase
import com.example.todoapplication.data.Tasks
import com.example.todoapplication.databinding.ActivityAddNewTasksBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddNewTasks : AppCompatActivity() {

    lateinit var database: TaskDatabase

    lateinit var binding: ActivityAddNewTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database =
            Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "taskDB").build()

        binding.doneBtn.setOnClickListener {
            val taskTitle = binding.addTitleEdTv.text.toString()
            val taskDes = binding.addSubTitleEdTv.text.toString()

            if (taskTitle.isNotEmpty() && taskDes.isNotEmpty()) {
                val tasks = Tasks(0, taskTitle, taskDes)
                lifecycleScope.launch {
                    database.tasksDao().insertTask(Tasks(0, taskTitle, taskDes))
                    finish()
                }


            }
        }


    }
}