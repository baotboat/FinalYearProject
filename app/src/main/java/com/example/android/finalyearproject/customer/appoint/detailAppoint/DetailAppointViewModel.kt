package com.example.android.finalyearproject.customer.appoint.detailAppoint

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.SingleEvent
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.repository.TaskFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.storage.StorageReference

class DetailAppointViewModel(
    private val taskRepo: TaskRepository,
    private val userRepo: UserRepository,
    private val storageRef: StorageReference
) : ViewModel()  {

    private var _taskDataiveData = MutableLiveData<Task?>()
    val taskDataLiveData: LiveData<Task?>
        get() = _taskDataiveData

    private var _updatePayoutLiveData = MutableLiveData< SingleEvent<Task?>>()
    val updatePayoutLiveData: MutableLiveData<SingleEvent<Task?>>
        get() = _updatePayoutLiveData

    fun setTaskData(taskData: Task) {
        _taskDataiveData.value = taskData
    }

    fun updatePayoutTask(taskId: String) {
        taskRepo.updateTaskRealtimeListener(
            taskId,
            object : TaskFirebaseCallback {
                override fun onResponse(taskData: Task) {
                    _updatePayoutLiveData.postValue(SingleEvent(taskData))
                }
            })
    }

    fun updateAlreadyPaid(taskId: String) {
        taskRepo.updateAlreadyPaidTask(taskId)
    }

    fun updateTaskStatus(taskId: String) {
        taskRepo.updateStatusTask(taskId, "งานซ่อมสำเร็จ")
    }

    fun dismissTask(taskId: String, uId: String, view: View) {
        userRepo.getUserByUId(uId) { userData ->
            if (userData.rating != null && userData.rating!! >= 10) {
                userRepo.decreaseRatingByDismissAppoint(uId)
            } else {
                userRepo.setUserRatingByDismissAppoint(uId, 0f)
            }
        }
        userRepo.updateUserFailedTask(uId)
        storageRef.child("images/$taskId").delete()
        taskRepo.deleteTaskByTaskIdGoToAppoint(taskId, view)
    }
}
//    fun dismissTask(taskId: String, uId: String, view: View) {
//        userRepo.getUserByUIdCallback(uId, object : UserFirebaseCallback {
//            override fun onResponse(userData: User) {
//                if (userData.rating != null && userData.rating!! >= 10) {
//                    userRepo.decreaseRatingByDismissAppoint(uId)
//                } else {
//                    userRepo.setUserRatingByDismissAppoint(uId, 0f)
//                }
//            }
//        })
//        userRepo.updateUserFailedTask(uId)
//        storageRef.child("images/$taskId").delete()
//        taskRepo.deleteTaskByTaskIdGoToAppoint(taskId, view)
//    }