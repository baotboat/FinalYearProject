package com.example.android.finalyearproject.sign.mech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterMechViewmodel(private val repository: UserRepository) : ViewModel() {

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.addUser(user)
        }
    }
}