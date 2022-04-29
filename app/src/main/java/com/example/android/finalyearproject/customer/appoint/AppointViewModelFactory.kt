package com.example.android.finalyearproject.customer.appoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AppointViewModelFactory(private val taskRepo: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointViewModel::class.java)) {
            return AppointViewModel(taskRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}