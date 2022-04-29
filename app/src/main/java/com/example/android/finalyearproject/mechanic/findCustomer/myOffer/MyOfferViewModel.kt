package com.example.android.finalyearproject.mechanic.findCustomer.myOffer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository

class MyOfferViewModel(val taskRepo: TaskRepository) : ViewModel() {

    private val _response = MutableLiveData<Response>()
    val response: LiveData<Response>
        get() = _response

    fun getTask() =
        taskRepo.getTasks(object : TaskListFirebaseCallback {
            override fun onResponse(response: Response) {
                _response.value = response
                response.taskList = null
                response.exception = null
            }
        })
}