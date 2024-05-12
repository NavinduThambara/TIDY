package com.example.tidy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tidy.database.TaskDatabase
import com.example.tidy.repository.TaskRepository
import com.example.tidy.viewmodel.TaskViewModel
import com.example.tidy.viewmodel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
    }

    private fun setupViewModel() {
        val taskRepository = TaskRepository(TaskDatabase(this))
        val viewModelProviderFactory = TaskViewModelFactory(application, taskRepository)
        taskViewModel = ViewModelProvider(this, viewModelProviderFactory)[TaskViewModel::class.java]
    }

}