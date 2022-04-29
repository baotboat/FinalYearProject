package com.example.android.finalyearproject.customer.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.finalyearproject.model.User
import com.example.android.finalyearproject.repository.UserRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileViewModel(private val userRepo: UserRepository) : ViewModel() {

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

    private var firebaseStorage = Firebase.storage
    var storageRef = firebaseStorage.reference

    fun getUser(uId: String) = userRepo.getUserByUId(uId) { userData ->
        _getUser.value = userData
    }

    fun changeUserData(uId: String, desc: String, address: String, phone:String) {
        userRepo.updateAddressUser(uId, address)
        userRepo.updateUserPhone(uId, phone)
        userRepo.updateDescUser(uId, desc) { complete ->
            _changeUserData.value = complete
        }
    }

    fun uploadImage(uri: Uri, uId: String) {
        storageRef.child("images/$uId").putFile(uri)
            .addOnFailureListener {

            }
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
}