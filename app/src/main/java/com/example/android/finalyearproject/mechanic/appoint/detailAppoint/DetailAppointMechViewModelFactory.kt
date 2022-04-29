package com.example.android.finalyearproject.mechanic.appoint.detailAppoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DetailAppointMechViewModelFactory(
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository,
    private val storageRef: StorageReference
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailAppointMechViewModel::class.java)) {
            return DetailAppointMechViewModel(taskRepo, userRepo, storageRef) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}