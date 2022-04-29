package com.example.android.finalyearproject.customer.appoint.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository

class RatingViewModel(val userRepo: UserRepository) : ViewModel() {

    private val _getUser = MutableLiveData<User>()
    val getUser: LiveData<User>
        get() = _getUser

    fun getUser(uId: String) {
        userRepo.getUserByUId(uId) { userData ->
            _getUser.value = userData
        }
    }

    fun updateUserRating(uId: String, rating: Float, countTimeRating: Int) {
        userRepo.updateUserRating(uId, rating, countTimeRating)
    }
}

