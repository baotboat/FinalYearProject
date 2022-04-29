package com.example.android.finalyearproject.customer.appoint.detailAppoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailAppointViewModelFactory(
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository,
    private val storageRef: StorageReference
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailAppointViewModel::class.java)) {
            return DetailAppointViewModel(taskRepo, userRepo, storageRef) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}