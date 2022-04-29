package com.example.android.finalyearproject.customer.findMech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FindMechViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindMechViewModel::class.java)) {
            return FindMechViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}