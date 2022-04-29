package com.example.android.finalyearproject.sign.mech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class RegisterMechViewmodelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterMechViewmodel::class.java)) {
            return RegisterMechViewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}