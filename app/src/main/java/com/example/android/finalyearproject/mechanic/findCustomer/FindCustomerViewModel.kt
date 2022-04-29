package com.example.android.finalyearproject.mechanic.findCustomer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository

class FindCustomerViewModel(
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private var _taskListLiveData = MutableLiveData<List<Task>?>()
    val taskListLiveData: LiveData<List<Task>?>
        get() = _taskListLiveData

    private val _getUserData = MutableLiveData<User>()
    val getUserData: LiveData<User>
        get() = _getUserData

    fun getTasksByFilters(filterTypeSelected: List<String>? = null, uId: String) {
        return taskRepo.getTasksByFilters(filterTypeSelected) { response ->
            selectTask(response, uId)
        }
    }

    private fun selectTask(response: Response, uId: String) {
        val taskListForUI = mutableListOf<Task>()
        val forFilterTypeAdapter = Task()
        taskListForUI.add(forFilterTypeAdapter)
        val cutOutWhoAlreadySentOffer = response.taskList?.reversed()?.filterNot {
            it.listWhoSentOffer?.contains(Task.Decision(
                mechUId = uId,
                decistion = "เสนอราคา"
            )) == true ||
            it.listWhoSentOffer?.contains(Task.Decision(
                mechUId = uId,
                decistion = "ปฏิเสธ"
            )) == true ||
            it.status != "กำลังหาช่าง"
        }
        cutOutWhoAlreadySentOffer?.onEach { taskListForUI.add(it) }
        _taskListLiveData.value = taskListForUI.toList()
        _taskListLiveData.value = null
    }

    fun getUserByUId(uId: String) = userRepo.getUserByUId(uId) { userData ->
        _getUserData.value = userData
    }
}
//    fun getTasksByFilters() {
//        _response.value = repository.getTasksByFilters().value
//    }

//    fun getUserByUId(uId: String) = userRepo.getUserByUId(uId)
