package com.example.android.finalyearproject.repository

import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.example.android.finalyearproject.customer.appoint.detailAppoint.DetailAppointFragmentDirections
import com.example.android.finalyearproject.customer.findMech.detailTask.DetailTaskFindMechFragmentDirections
import com.example.android.finalyearproject.mechanic.appoint.detailAppoint.DetailAppointMechFragmentDirections
import com.example.android.finalyearproject.model.Response
import com.example.android.finalyearproject.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TaskRepository {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private var user: FirebaseUser? = null

    fun addTask(task: Task) {
        firebaseDB.collection("Task").document(task.taskId!!).set(task)
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
                val error = e.message.toString()
                Log.e("error", error)
            }
    }

    fun getTasksByUId(callbackTaskList: TaskListFirebaseCallback) {
        user = FirebaseAuth.getInstance().currentUser
        var response = Response()
        firebaseDB.collection("Task").whereEqualTo("creatorUId", user?.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.taskList = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(Task::class.java)
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callbackTaskList.onResponse(response)
            }
    }

    fun getTasksByStatus(status: String, callbackTaskList: TaskListFirebaseCallback) {
        var response = Response()
        firebaseDB.collection("Task").whereEqualTo("status", status)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.taskList = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(Task::class.java)
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callbackTaskList.onResponse(response)
            }
    }

    fun getTasksByChipSymptom(chipSymptom: String, callbackTaskList: TaskListFirebaseCallback) {
        var response = Response()
        firebaseDB.collection("Task").whereEqualTo("chipSymptom", chipSymptom)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.taskList = result.documents.mapNotNull { snapShot ->
                            snapShot.toObject(Task::class.java)
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callbackTaskList.onResponse(response)
            }
    }

    fun getTasksBytaskId(callbackTaskList: TaskListFirebaseCallback, taskId: String) {
        val response = Response()
        var taskList = mutableListOf<Task>()
        firebaseDB.collection("Task").whereEqualTo("taskId", taskId)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    taskList = result.toObjects(Task::class.java)
                }
                response.taskList = taskList.toList()
                callbackTaskList.onResponse(response)
            }
    }

    fun getTasksByFilters(
        filter: List<String>? = null,
        callbackTaskList: (Response) -> Unit
    ) {
        val response = Response()
        val taskList = mutableListOf<Task>()
        if (filter == null) {
            firebaseDB.collection("Task").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document != null) {
                            taskList.add(document.toObject(Task::class.java))
                        }
                    }
                    response.taskList = taskList.toList()
                    callbackTaskList.invoke(response)
                }
        } else {
            firebaseDB.collection("Task").whereIn("type", filter)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document != null) {
                            taskList.add(document.toObject(Task::class.java))
                        }
                    }
                    response.taskList = taskList.toList()
                    callbackTaskList.invoke(response)
                }
        }
    }

    fun getTasks(callbackTaskList: TaskListFirebaseCallback) {
        val response = Response()
        val taskList = mutableListOf<Task>()
        firebaseDB.collection("Task").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document != null) {
                        taskList.add(document.toObject(Task::class.java))
                    }
                }
                response.taskList = taskList.toList()
                callbackTaskList.onResponse(response)
            }
    }

    fun addListOfferByTaskId(taskId: String, offer: Task.Offer) {
        firebaseDB.collection("Task").document(taskId)
            .update("listOffer", FieldValue.arrayUnion(offer))
    }

    fun removeListOfferByTaskId(taskId: String, offer: Task.Offer) {
        firebaseDB.collection("Task").document(taskId)
            .update("listOffer", FieldValue.arrayRemove(offer))
    }

    fun addWhoSentOfferByTaskId(taskId: String, decision: Task.Decision) {
        firebaseDB.collection("Task").document(taskId)
            .update("listWhoSentOffer", FieldValue.arrayUnion(decision))
    }

    fun removeWhoSentOfferByTaskId(taskId: String, decision: Task.Decision) {
        firebaseDB.collection("Task").document(taskId)
            .update("listWhoSentOffer", FieldValue.arrayRemove(decision))
    }

    fun updateStatusTask(taskId: String, status: String) {
        firebaseDB.collection("Task").document(taskId)
            .update("status", status)
    }

    fun updateCustomerSelectedTask(
        taskId: String,
        oldOffer: Task.Offer,
        newOffer: Task.Offer,
    ) {
        firebaseDB.collection("Task").document(taskId)
            .update("listOffer", FieldValue.arrayRemove(oldOffer))

        firebaseDB.collection("Task").document(taskId)
            .update("listOffer", FieldValue.arrayUnion(newOffer))
    }

    fun deleteTaskByTaskIdGoToFindMech(taskId: String, view: View) {
        firebaseDB.collection("Task").document(taskId)
            .delete()
            .addOnCompleteListener {
                view.findNavController()
                    .navigate(DetailTaskFindMechFragmentDirections.actionDetailTaskFindMechFragmentToNavigationFindMechanic())
            }
    }

    fun updateTaskRealtimeListener(taskId: String, callbackTask: TaskFirebaseCallback) {
        firebaseDB.collection("Task").document(taskId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(Task::class.java)?.let { callbackTask.onResponse(it) }
                } else {
                    Log.d("TAG", "Current data: null")
                }
            }
    }

    fun updateConfirmPriceTask(taskId: String, price: String) {
        firebaseDB.collection("Task").document(taskId)
            .update("confirmPrice", price)
    }

    fun updateAlreadyPaidTask(taskId: String) {
        firebaseDB.collection("Task").document(taskId)
            .update("alreadyPaid", true)
    }

    fun deleteTaskByTaskIdGoToAppoint(taskId: String, view: View) {
        firebaseDB.collection("Task").document(taskId)
            .delete()
            .addOnCompleteListener {
                view.findNavController().navigate(DetailAppointFragmentDirections
                    .actionDetailAppointFragmentToNavigationAppoint())
            }
    }

    fun deleteTaskByTaskIdGoToAppointMech(taskId: String, view: View) {
        firebaseDB.collection("Task").document(taskId)
            .delete()
            .addOnCompleteListener {
                view.findNavController()
                    .navigate(DetailAppointMechFragmentDirections.actionDetailAppointMechFragmentToNavigationAppointMech())
            }
    }

    fun updateFinishPayoutTask(taskId: String) {
        firebaseDB.collection("Task").document(taskId)
            .update("finishPayout", true)
    }
}
//    fun getTasksByFilters(): MutableLiveData<Response> {
//        val mutableLiveData = MutableLiveData<Response>()
//        firebaseDB.collection("Task")
//            .get()
//            .addOnCompleteListener { task ->
//                val response = Response()
//                if (task.isSuccessful) {
//                    val result = task.result
//                    result?.let {
//                        response.taskList = result.documents.mapNotNull { snapShot ->
//                            snapShot.toObject(Task::class.java)
//                        }
//                    }
//                } else {
//                    response.exception = task.exception
//                }
//                mutableLiveData.value = response
//            }
//
//        return mutableLiveData
//    }