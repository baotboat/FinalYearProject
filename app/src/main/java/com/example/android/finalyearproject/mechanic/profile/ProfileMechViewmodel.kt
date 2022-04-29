package com.example.android.finalyearproject.mechanic.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.TaskListFirebaseCallback
import com.example.android.finalyearproject.repository.TaskRepository
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileMechViewmodel(
    private val userRepo: UserRepository,
    private val taskRepo: TaskRepository,
) : ViewModel() {

    private val _getUser = MutableLiveData<User>()
    val getUser: LiveData<User>
        get() = _getUser

    private val _changeUserData = MutableLiveData<Boolean>()
    val changeUserData: LiveData<Boolean>
        get() = _changeUserData

    private val _loadProfileImageCompleted = MutableLiveData<ByteArray?>()
    val loadProfileImageCompleted: LiveData<ByteArray?>
        get() = _loadProfileImageCompleted

    private val _loadProfileImageFailed = MutableLiveData<Boolean>()
    val loadProfileImageFailed: LiveData<Boolean>
        get() = _loadProfileImageFailed

    private var _taskListLiveData = MutableLiveData<List<Task>?>()
    val taskListLiveData: LiveData<List<Task>?>
        get() = _taskListLiveData

    private var firebaseStorage = Firebase.storage
    var storageRef = firebaseStorage.reference

    fun getUser(uId: String) {
        userRepo.getUserByUId(uId) { userData ->
            _getUser.value = userData
        }
    }

    fun changeUserData(uId: String, desc: String, type: List<String>?, address: String, phone:String) {
        userRepo.updateTypeUser(uId, type)
        userRepo.updateAddressUser(uId, address)
        userRepo.updateUserPhone(uId, phone)
        userRepo.updateDescUser(uId, desc) { complete ->
            _changeUserData.value = complete
        }
    }

    fun uploadImage(uri: Uri, uId: String) {
        storageRef.child("images/$uId").putFile(uri)
            .addOnSuccessListener {
                loadImage(uId)
            }
    }

    fun loadImage(uId: String) {
        val storageReference = firebaseStorage.reference.child("images/$uId")
        val IMAGE_SIZE: Long = 4096 * 4096
        storageReference.getBytes(IMAGE_SIZE)
            .addOnSuccessListener {
                _loadProfileImageCompleted.value = it
                _loadProfileImageCompleted.value = null
            }
            .addOnFailureListener {
                _loadProfileImageFailed.value = true
            }
    }

    fun getTasksByStatus(uId: String) = taskRepo.getTasksByStatus(
        "งานซ่อมสำเร็จ",
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