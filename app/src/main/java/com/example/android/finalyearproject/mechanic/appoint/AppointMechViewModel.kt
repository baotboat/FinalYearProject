package com.example.android.finalyearproject.mechanic.appoint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository

class AppointMechViewModel(private val taskRepo: TaskRepository) : ViewModel() {

    private var _taskListLiveData = MutableLiveData<List<Task>?>()
    val taskListLiveData: LiveData<List<Task>?>
        get() = _taskListLiveData

    fun getTasksByStatus(uId: String) = taskRepo.getTasksByStatus(
        "นัดหมายสำเร็จ",
        object : TaskListFirebaseCallback {
            override fun onResponse(response: Response) {
                selectTask(response, uId)
            }
        })

    private fun selectTask(response: Response, uId: String) {
        val taskList = response.taskList?.reversed()
        val filterTaskList = mutableListOf<Task>()
        taskList?.onEach { task ->
            task.listOffer?.onEach {
                if (it.customerSelected == true && it.mechUId == uId) {
                    filterTaskList.add(task)
                }
            }
        }
        _taskListLiveData.value = filterTaskList
        _taskListLiveData.value = null
    }

}