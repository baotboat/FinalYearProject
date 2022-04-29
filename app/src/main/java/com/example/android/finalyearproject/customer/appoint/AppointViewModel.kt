package com.example.android.finalyearproject.customer.appoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository

class AppointViewModel(private val taskRepo: TaskRepository) : ViewModel() {

    private var _taskListLiveData = MutableLiveData<List<Task>?>()
    val taskListLiveData: LiveData<List<Task>?>
        get() = _taskListLiveData

    fun getTasksByUId() = taskRepo.getTasksByUId(
        object : TaskListFirebaseCallback {
            override fun onResponse(response: Response) {
                selectTask(response)
            }
        })

    private fun selectTask(response: Response) {
        val taskList = response.taskList?.reversed()?.filter {
            it.status == "นัดหมายสำเร็จ"
        }
        _taskListLiveData.value = taskList
        _taskListLiveData.value = null
    }
}