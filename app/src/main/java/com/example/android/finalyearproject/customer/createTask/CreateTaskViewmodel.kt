package com.example.android.finalyearproject.customer.createTask

import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.android.finalyearproject.R
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class CreateTaskViewmodel(
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository,
) : ViewModel() {

    private val _getUserData = MutableLiveData<User>()
    val getUserData: LiveData<User>
        get() = _getUserData

    var firebaseStorage = Firebase.storage
    var storageRef = firebaseStorage.reference

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepo.addTask(task)
        }
    }

    fun uploadImage(uri: Uri, taskId: String, view: View) {
        storageRef.child("images/$taskId").putFile(uri)
            .addOnFailureListener {

            }
            .addOnSuccessListener {
                view.findNavController()
                    .navigate(R.id.action_createTaskFragment_to_navigation_find_mechanic)
            }
    }

    fun getUserByUId(uId: String) = userRepo.getUserByUId(uId) { userData ->
        _getUserData.value = userData
    }
}