package com.example.android.finalyearproject.repository

import android.util.Log
import com.example.android.finalyearproject.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {

    private val firebaseDB = Firebase.firestore

    fun getUserByUId(uId: String, userDataCallback: (User) -> Unit) {
        firebaseDB.collection("User").document(uId).get()
            .addOnCompleteListener { task ->
                var response = User()
                if (task.isSuccessful) {
                    val result = task.result
                    if (result.data != null) {
                        response = result.toObject(User::class.java)!!
                    }
                }
                userDataCallback.invoke(response)
            }
    }

    fun addUser(user: User) {
        user.uId?.let {
            firebaseDB.collection("User").document(user.uId!!).set(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        }
    }

    fun updateTypeUser(uId: String, type: List<String>?) {
        if (type.isNullOrEmpty()) {
            firebaseDB.collection("User").document(uId)
                .update("type", null)
        } else {
            firebaseDB.collection("User").document(uId)
                .update("type", null)
            firebaseDB.collection("User").document(uId)
                .update("type", type)
        }
    }

    fun updateAddressUser(uId: String, address: String) {
        firebaseDB.collection("User").document(uId)
            .update("address", address)
    }

    fun updateUserPhone(uId: String, phone: String) {
        firebaseDB.collection("User").document(uId)
            .update("phone", phone)
    }

    fun updateDescUser(uId: String, desc: String, callback: (Boolean) -> Unit) {
        firebaseDB.collection("User").document(uId)
            .update("desc", desc)
            .addOnCompleteListener {
                callback.invoke(true)
            }
    }

    fun updateUserRating(uId: String, rating: Float, countTimeRating: Int) {
        firebaseDB.collection("User").document(uId)
            .update("rating", rating)

        firebaseDB.collection("User").document(uId)
            .update("countTimeRating", countTimeRating)
    }

    fun decreaseRatingByDismissAppoint(uId: String) {
        firebaseDB.collection("User").document(uId)
            .update("rating", FieldValue.increment(-10))
    }

    fun setUserRatingByDismissAppoint(uId: String, rating: Float) {
        firebaseDB.collection("User").document(uId)
            .update("rating", rating)
    }

    fun updateUserSucceedTask(mechUId: String, customerUId: String) {
        firebaseDB.collection("User").document(mechUId)
            .update("succeedTask", FieldValue.increment(1))

        firebaseDB.collection("User").document(customerUId)
            .update("succeedTask", FieldValue.increment(1))
    }

    fun updateUserFailedTask(uId: String) {
        firebaseDB.collection("User").document(uId)
            .update("failedTask", FieldValue.increment(1))
    }

//    fun getUser(uId: String): MutableLiveData<User> {
//        val mutableLiveData = MutableLiveData<User>()
//        var result = User()
//        firebaseDB.collection("User").document(uId).get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    result = document.toObject(User::class.java)!!
//                } else {
//
//                }
//                mutableLiveData.value = result
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Log", "get failed with ", exception)
//            }
//
//        return mutableLiveData
//    }
}