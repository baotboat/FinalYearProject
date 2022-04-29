package com.example.android.finalyearproject.customer.findMech.referTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository

class ReferTaskViewModel(private val taskRepo: TaskRepository) : ViewModel() {

    private var _taskListLiveData = MutableLiveData<List<Task>?>()
    val taskListLiveData: LiveData<List<Task>?>
        get() = _taskListLiveData

    fun getTasksByChipSymptom(chipSymptom: String, type: String, spec: String) =
        taskRepo.getTasksByChipSymptom(
            chipSymptom,
            object : TaskListFirebaseCallback {
                override fun onResponse(response: Response) {
                    selectTask(response, type, spec)
                }
            })

    private fun selectTask(response: Response, type: String, spec: String) {
        val taskList = response.taskList?.reversed()
        var filterTaskList = taskList?.filter { it.spec == spec && it.status == "งานซ่อมสำเร็จ"}?.toMutableList()
        taskList?.filter { it.spec != spec && it.type == type && it.status == "งานซ่อมสำเร็จ"}?.onEach {
            filterTaskList?.add(it)
        }
        _taskListLiveData.value = filterTaskList
        _taskListLiveData.value = null
    }
}