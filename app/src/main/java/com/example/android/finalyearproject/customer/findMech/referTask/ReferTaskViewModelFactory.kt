package com.example.android.finalyearproject.customer.findMech.referTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ReferTaskViewModelFactory(private val taskRepo: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReferTaskViewModel::class.java)) {
            return ReferTaskViewModel(taskRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}