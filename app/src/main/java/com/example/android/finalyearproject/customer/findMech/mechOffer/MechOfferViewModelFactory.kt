package com.example.android.finalyearproject.customer.findMech.mechOffer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.finalyearproject.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MechOfferViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MechOfferViewModel::class.java)) {
            return MechOfferViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}