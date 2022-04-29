package com.example.android.finalyearproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository

class MainActivityViewModel(private val userRepo: UserRepository) : ViewModel() {

    private val _getUserData = MutableLiveData<User>()
    val getUserData: LiveData<User>
        get() = _getUserData

    fun getUser(uId: String) = userRepo.getUserByUId(uId) { userData ->
        _getUserData.value = userData
    }
}