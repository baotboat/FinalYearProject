package com.example.android.finalyearproject.mechanic.findCustomer.myOffer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MyOfferViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyOfferViewModel::class.java)) {
            return MyOfferViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}