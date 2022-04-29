package com.example.android.finalyearproject.customer.findMech.mechOffer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository

class MechOfferViewModel(val taskRepo: TaskRepository) : ViewModel() {
    private val _response = MutableLiveData<Response>()
    val response: LiveData<Response>
        get() = _response

    fun getTasksBytaskId(taskId : String) =
        taskRepo.getTasksBytaskId(object : TaskListFirebaseCallback {
            override fun onResponse(response: Response) {
                _response.value = response
                response.taskList = null
                response.exception = null
            }
        },
            taskId
        )
}