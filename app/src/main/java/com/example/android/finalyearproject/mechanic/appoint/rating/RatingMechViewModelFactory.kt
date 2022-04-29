package com.example.android.finalyearproject.mechanic.appoint.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class RatingMechViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingMechViewModel::class.java)) {
            return RatingMechViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}