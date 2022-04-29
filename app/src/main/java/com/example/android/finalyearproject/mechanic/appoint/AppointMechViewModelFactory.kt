package com.example.android.finalyearproject.mechanic.appoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AppointMechViewModelFactory(private val taskRepo: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointMechViewModel::class.java)) {
            return AppointMechViewModel(taskRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}