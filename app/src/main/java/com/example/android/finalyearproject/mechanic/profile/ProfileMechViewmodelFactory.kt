package com.example.android.finalyearproject.mechanic.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ProfileMechViewmodelFactory(
    private val userRepo: UserRepository,
    private val taskRepo: TaskRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileMechViewmodel::class.java)) {
            return ProfileMechViewmodel(userRepo, taskRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}